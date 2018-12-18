<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" version="1.0" encoding="UTF-8" indent="no"/>
	<xsl:strip-space elements="*"/>
	
	<xsl:template match="RobotConfigTaxonomy"><![CDATA[<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="text" indent="no"/>
	<xsl:strip-space elements="*"/>

	<xsl:variable name="nl"><xsl:text>
</xsl:text></xsl:variable>
	<xsl:variable name="spaces" select= "'                                                                                   '"/>
	<xsl:variable name="space" select= "' '"/>
  ]]><xsl:apply-templates/><![CDATA[
  	<xsl:template match="MotorController">
		<xsl:param name="indent" select="0"/>
		<xsl:value-of select="substring($spaces, 1, $indent)"/>
		<xsl:choose>
			<xsl:when test="@port">a Motor Controller <xsl:choose>
			<xsl:when test="@name">(<xsl:value-of select="@name"/>) </xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>in port <xsl:value-of select="@port"/>,<xsl:value-of select="$nl"/><xsl:value-of select="substring($spaces, 1, $indent)"/>with</xsl:when>
			<xsl:otherwise>a USB Motor Controller<xsl:choose>
			<xsl:when test="@name"> (<xsl:value-of select="@name"/>)</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>,<xsl:value-of select="$nl"/><xsl:value-of select="substring($spaces, 1, $indent)"/>with</xsl:otherwise>
		</xsl:choose>
		<xsl:value-of select="$nl"/>
		<xsl:apply-templates>
			<xsl:with-param name="indent" select="$indent+4"/>
		</xsl:apply-templates>
	</xsl:template>
  
	<xsl:template match="ServoController">
		<xsl:param name="indent" select="0"/>
		<xsl:value-of select="substring($spaces, 1, $indent)"/>
		<xsl:choose>
			<xsl:when test="@port">a Servo Controller <xsl:choose>
			<xsl:when test="@name">(<xsl:value-of select="@name"/>) </xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>in port <xsl:value-of select="@port"/>,<xsl:value-of select="$nl"/><xsl:value-of select="substring($spaces, 1, $indent)"/>with</xsl:when>
			<xsl:otherwise>a USB Servo Controller<xsl:choose>
			<xsl:when test="@name"> (<xsl:value-of select="@name"/>)</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>,<xsl:value-of select="$nl"/><xsl:value-of select="substring($spaces, 1, $indent)"/>with</xsl:otherwise>
		</xsl:choose>
		<xsl:value-of select="$nl"/>
		<xsl:apply-templates>
			<xsl:with-param name="indent" select="$indent+4"/>
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template match="LegacyModuleController">
		<xsl:param name="indent" select="0"/>
		<xsl:value-of select="substring($spaces, 1, $indent)"/>a Legacy Controller<xsl:choose>
			<xsl:when test="@name"> (<xsl:value-of select="@name"/>)</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>,<xsl:value-of select="$nl"/><xsl:value-of select="substring($spaces, 1, $indent)"/>with<xsl:value-of select="$nl"/>
		<xsl:apply-templates>
			<xsl:with-param name="indent" select="$indent+4"/>
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template match="DeviceInterfaceModule">
		<xsl:param name="indent" select="0"/>
		<xsl:value-of select="substring($spaces, 1, $indent)"/>a Core Device Interface Module<xsl:choose>
			<xsl:when test="@name"> (<xsl:value-of select="@name"/>)</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>,<xsl:value-of select="$nl"/><xsl:value-of select="substring($spaces, 1, $indent)"/>with<xsl:value-of select="$nl"/>
		<xsl:apply-templates>
			<xsl:with-param name="indent" select="$indent+4"/>
			<xsl:with-param name="digitalbus">digital port</xsl:with-param>
			<xsl:with-param name="i2cbus">I2C port</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
  
</xsl:stylesheet>
]]></xsl:template>

	<xsl:template match="Sensor"><![CDATA[
    <xsl:template match="]]><xsl:value-of select="XmlTag"/><![CDATA[">
		<xsl:param name="indent" select="0"/>
		<xsl:param name="]]><xsl:value-of select="Bus"/><![CDATA[">]]><xsl:value-of select="BusDefault"/><![CDATA[</xsl:param>
		<xsl:value-of select="substring($spaces, 1, $indent)"/>]]><xsl:value-of select="Description"/><![CDATA[ <xsl:choose>
			<xsl:when test="@name">(<xsl:value-of select="@name"/>) </xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>in <xsl:value-of select="$]]><xsl:value-of select="Bus"/><![CDATA["/><xsl:value-of select="$space"/><xsl:value-of select="@port"/>
		<xsl:value-of select="$nl"/>
	</xsl:template>]]>
</xsl:template>

</xsl:stylesheet>
