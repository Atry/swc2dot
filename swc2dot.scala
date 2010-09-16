#!/bin/env scala
#
# Copyright (c) 2010 , Yang Bo. All rights reserved.
#
# Author: Yang Bo (pop.atry@gmail.com)
#
# Use, modification and distribution are subject to the "GPL v3"
# as listed at <url: http://www.gnu.org/licenses/gpl.html >.
!#

def packageOf(className:String):String = {
	val index = className.indexOf(':')
	return if (index == -1) "__GLOBAL__" else className.substring(0, index)
}

val swc = scala.xml.XML.load(System.in)
val scripts = swc\"libraries"\"library"\"script"
var grouped = scripts.groupBy {
	n:xml.Node=>packageOf((n\"def"\"@id").toString())
}
println("digraph swc {")
for ((packageName, scripts) <- grouped) {
	for (deppackageName:String <- (for (depid <- scripts\"dep") yield packageOf((depid\"@id").toString())).toSet) {
		if (!(deppackageName.startsWith("flash.") || deppackageName.startsWith("mx.") || deppackageName == "__GLOBAL__" || deppackageName == packageName))
		{
			println("\t\"" + packageName + "\" -> \"" + deppackageName + "\";")
		}
	}
}
println("}")
