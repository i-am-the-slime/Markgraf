import android.Keys._

android.Plugin.androidBuild

name := "markgraf"

platformTarget in Android := "android-19"

scalaVersion := "2.10.3"
 
scalacOptions in Compile += "-feature"

//libraryDependencies += "com.google.guava" % "guava" % "r09"

proguardOptions in Android ++= Seq(
  "-keep public class * extends junit.framework.TestCase",
  "-keepclassmembers class * extends junit.framework.TestCase { *; }"
)
 
run <<= run in Android
 
install <<= install in Android
