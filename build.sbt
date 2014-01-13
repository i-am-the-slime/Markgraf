import android.Keys._

android.Plugin.androidBuild

name := "markgraf"

platformTarget in Android := "android-19"

scalaVersion := "2.10.3"
 
scalacOptions in Compile += "-feature"

libraryDependencies += "org.joda" % "joda-convert" % "1.5"

libraryDependencies += "joda-time" % "joda-time" % "2.3"

libraryDependencies += "com.googlecode.lambdaj" % "lambdaj" % "2.3.3"

proguardOptions in Android ++= Seq(
  "-keep public class * extends junit.framework.TestCase",
  "-keepclassmembers class * extends junit.framework.TestCase { *; }",
  "-dontwarn javax.xml.bind.*"
)
 
run <<= run in Android
 
install <<= install in Android
