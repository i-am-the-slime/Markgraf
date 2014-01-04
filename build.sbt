import android.Keys._

android.Plugin.androidBuild

name := "markgraf" // CHANGE THIS

platformTarget in Android := "android-19"

scalaVersion := "2.10.3"
 
scalacOptions in Compile += "-feature"

proguardOptions in Android ++= Seq(
  "-keep public class * extends junit.framework.TestCase",
  "-keepclassmembers class * extends junit.framework.TestCase { *; }"
)
 
run <<= run in Android
 
install <<= install in Android
