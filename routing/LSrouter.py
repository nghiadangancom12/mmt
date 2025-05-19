import time
from router import Router
from packet import Packet
import heapq


class LSrouter(Router):
    def __init__(self, addr, heartbeat_time):
        super().__init__(addr)
        self.heartbeat_time = heartbeat_time
        self.last_time = 0

        # Bản đồ mạng: addr -> (sequence_number, {neighbor: cost})
        self.link_state_db = {self.addr: (0, {})}

        # Bản đồ cổng kết nối: neighbor -> (port, cost)
        self.neighbors = {}

        # Bảng định tuyến: đích -> cổng gửi
        self.forwarding_table = {}

        # Ghi nhớ số thứ tự gói tin cuối cùng của mỗi router
        self.sequence_numbers = {self.addr: 0}

    def _broadcast_link_state(self):
        """Gửi gói tin trạng thái liên kết của router này đến tất cả hàng xóm."""
        seq_num, neighbors = self.link_state_db[self.addr]
        content = f"{self.addr}|{seq_num}|{','.join([f'{n}:{c}' for n, c in neighbors.items()])}"
        packet = Packet(Packet.ROUTING, self.addr, None, content)
        for port in self.links:
            self.send(port, packet)

    def _recompute_forwarding_table(self):
        """Cập nhật bảng định tuyến bằng thuật toán Dijkstra."""
        graph = {}
        for node, (_, neighbors) in self.link_state_db.items():
            graph[node] = neighbors.copy()

        dist = {self.addr: 0}
        prev = {}
        visited = set()
        heap = [(0, self.addr)]

        while heap:
            cost, node = heapq.heappop(heap)
            if node in visited:
                continue
            visited.add(node)
            for neighbor, weight in graph.get(node, {}).items():
                if neighbor not in dist or cost + weight < dist[neighbor]:
                    dist[neighbor] = cost + weight
                    prev[neighbor] = node
                    heapq.heappush(heap, (cost + weight, neighbor))

        self.forwarding_table = {}
        for dst in dist:
            if dst == self.addr:
                continue
            hop = dst
            while prev.get(hop) != self.addr:
                hop = prev.get(hop)
                if hop is None:
                    break
            if hop in self.neighbors:
                self.forwarding_table[dst] = self.neighbors[hop][0]

    def handle_packet(self, port, packet):
        if packet.kind == Packet.TRACEROUTE:
            dst = packet.dst_addr
            if dst in self.forwarding_table:
                out_port = self.forwarding_table[dst]
                self.send(out_port, packet)
        elif packet.kind == Packet.ROUTING:
            # Giải mã gói tin trạng thái liên kết
            try:
                content = packet.content
                src, seq_str, neigh_str = content.split("|")
                seq = int(seq_str)
                neighbors = {}
                if neigh_str:
                    for part in neigh_str.split(","):
                        n, c = part.split(":")
                        neighbors[n] = int(c)
            except Exception:
                return

            if src not in self.sequence_numbers or seq > self.sequence_numbers[src]:
                self.sequence_numbers[src] = seq
                self.link_state_db[src] = (seq, neighbors)
                self._recompute_forwarding_table()

                # Lan truyền gói tin đến các hàng xóm (trừ nơi đã nhận)
                for p in self.links:
                    if p != port:
                        self.send(p, packet)

    def handle_new_link(self, port, endpoint, cost):
        self.neighbors[endpoint] = (port, cost)
        self.sequence_numbers[self.addr] += 1
        self.link_state_db[self.addr] = (
            self.sequence_numbers[self.addr],
            {n: c for n, (_, c) in self.neighbors.items()},
        )
        self._recompute_forwarding_table()
        self._broadcast_link_state()

    def handle_remove_link(self, port):
        # Tìm và loại bỏ hàng xóm tương ứng với port này
        removed = None
        for neighbor, (p, _) in self.neighbors.items():
            if p == port:
                removed = neighbor
                break
        if removed:
            del self.neighbors[removed]
            self.sequence_numbers[self.addr] += 1
            self.link_state_db[self.addr] = (
                self.sequence_numbers[self.addr],
                {n: c for n, (_, c) in self.neighbors.items()},
            )
            self._recompute_forwarding_table()
            self._broadcast_link_state()

    def handle_time(self, time_ms):
        if time_ms - self.last_time >= self.heartbeat_time:
            self.last_time = time_ms
            self._broadcast_link_state()

    def __repr__(self):
        return f"LSrouter(addr={self.addr}, forwarding_table={self.forwarding_table})"
