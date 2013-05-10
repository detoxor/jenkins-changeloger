<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" doctype-system="about:legacy-compat" indent="yes" />
	<xsl:template match="/">
		<html>
			<head>
				<title>Neco</title>
				<style type="text/css">
*{padding:0;margin:0; font-family:Verdana; }
body {margin:0 auto; padding:5px; }
h2, h4 {padding: 5px; background-color: #efefef;}
p {font-size:12px; padding:2px 0px;}
.item {border-bottom:1px solid #ccc; padding:5px;}
.item p {margin:5px 0px; font-size:11px;}
.header{ padding:5px; }
				</style>
			</head>
			<body>
			<h2>Build <xsl:value-of select="mavenModuleSetBuild/fullDisplayName"/></h2>
			<div class="header">
			<p><b>Generated</b>: <xsl:call-template name="date-formatter">
		    	<xsl:with-param name="dt"><xsl:value-of select="mavenModuleSetBuild/id"/></xsl:with-param>
			</xsl:call-template></p>
			<p><b>Link</b>: <a href="{mavenModuleSetBuild/url}"><xsl:value-of select="mavenModuleSetBuild/url"/></a></p>
			<p><b>Result</b>: <xsl:value-of select="mavenModuleSetBuild/result"/></p><br/>
			</div>
			<h4>Changes</h4>
			<xsl:apply-templates select="mavenModuleSetBuild/changeSet/item"/>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="/mavenModuleSetBuild/changeSet/item">
		<div class="item">
			<p><xsl:call-template name="date-formatter">
		    	<xsl:with-param name="dt"><xsl:value-of select="date"/></xsl:with-param>
			</xsl:call-template></p>
			<p>Commit: <b><xsl:value-of select="commitId"/></b> by <b><xsl:value-of select="author/fullName"/></b></p>
			<p>File: 
				<a>
				<xsl:attribute name="href">http://localhost:8080/jenkins/job/JMokos/ws/<xsl:value-of select="path/file"/></xsl:attribute>
				<xsl:value-of select="affectedPath"/>
				</a>
			</p>
			<p><xsl:value-of select="msg"/></p>
		</div>
	</xsl:template>
	
	<xsl:template name="date-formatter">
		<xsl:param name="dt"/>
		<xsl:value-of select="concat(substring($dt, 9, 2),
	                      '-',
	                      substring($dt, 6, 2),
	                      '-',
	                      substring($dt, 1, 4),
	                      ' ',
	                      substring($dt, 12, 2),':',substring($dt, 15, 2),':',substring($dt, 18, 2)
	                      )"/>
	</xsl:template>
</xsl:stylesheet>