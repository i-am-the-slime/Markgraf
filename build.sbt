import android.Keys._

android.Plugin.androidBuild

name := "EXAMPLE" # CHANGE THIS

platformTarget in Android := "android-19"

scalaVersion := "2.10.3"
 
scalacOptions in Compile += "-feature"
 
run <<= run in Android
 
install <<= install in Android
