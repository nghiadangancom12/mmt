package com.example.unit2_pathway14

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.unit2_pathway14.ui.theme.Unit2_pathway14Theme

class MainActivity : ComponentActivity() {
    open class SmartDevice(val name:String,val category:String){
        var deviceStatus:String="tat"
        open val deviceType:String="khong ro"
        open fun turnOn(){ deviceStatus="bat"; println("$name da bat") }
        open fun turnOff(){ deviceStatus="tat"; println("$name da tat") }
        fun printDeviceInfo(){ println("Ten thiet bi: $name, loai: $category, kieu: $deviceType") }
    }

    class SmartTvDevice(name:String,category:String):SmartDevice(name,category){
        override val deviceType="Ti vi thong minh"; private var volume=10; private var channel=1
        fun increaseVolume(){ if(deviceStatus=="bat"){ volume++; println("Am luong ti vi tang len $volume") } }
        fun decreaseVolume(){ if(deviceStatus=="bat"){ volume--; println("Am luong ti vi giam xuong $volume") } }
        fun nextChannel(){ if(deviceStatus=="bat"){ channel++; println("Kenh ti vi doi sang $channel") } }
        fun previousChannel(){ if(deviceStatus=="bat"){ if(channel>1) channel--; println("Kenh ti vi doi sang $channel") } }
    }

    class SmartLightDevice(name:String,category:String):SmartDevice(name,category){
        override val deviceType="Den thong minh"; private var brightness=50
        fun increaseBrightness(){ if(deviceStatus=="bat"){ brightness+=10; println("Do sang den tang len $brightness") } }
        fun decreaseBrightness(){ if(deviceStatus=="bat"){ brightness-=10; println("Do sang den giam xuong $brightness") } }
    }

    class SmartHome(val smartTv: SmartTvDevice, val smartLight: SmartLightDevice) {
        var deviceTurnOnCount: Int = 0
            private set

        fun turnOnTv() {
            if (smartTv.deviceStatus != "bat") {
                smartTv.turnOn()
                deviceTurnOnCount++
            }
        }

        fun turnOffTv() {
            if (smartTv.deviceStatus == "bat") {
                smartTv.turnOff()
                deviceTurnOnCount--
            }
        }

        fun turnOnLight() {
            if (smartLight.deviceStatus != "bat") {
                smartLight.turnOn()
                deviceTurnOnCount++
            }
        }

        fun turnOffLight() {
            if (smartLight.deviceStatus == "bat") {
                smartLight.turnOff()
                deviceTurnOnCount--
            }
        }

        fun decreaseTvVolume() {
            smartTv.decreaseVolume()
        }

        fun changeTvChannelToPrevious() {
            smartTv.previousChannel()
        }

        fun decreaseLightBrightness() {
            smartLight.decreaseBrightness()
        }

        fun printSmartTvInfo() {
            smartTv.printDeviceInfo()
        }

        fun printSmartLightInfo() {
            smartLight.printDeviceInfo()
        }
    }

    fun main() {
        val tv=SmartTvDevice("Samsung TV","Giai tri"); val light=SmartLightDevice("Philips Light","Chieu sang")
        val smartHome=SmartHome(tv,light)
        smartHome.turnOnTv(); smartHome.turnOnLight()
        smartHome.decreaseTvVolume(); smartHome.changeTvChannelToPrevious(); smartHome.decreaseLightBrightness()
        smartHome.printSmartTvInfo(); smartHome.printSmartLightInfo()
        println("So thiet bi dang bat: ${smartHome.deviceTurnOnCount}")
        smartHome.turnOffTv(); smartHome.decreaseTvVolume()
    }

}