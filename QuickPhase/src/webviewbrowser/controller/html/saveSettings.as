IFSTREQUAL('{cfgSaveTo}','ini')
	INIWRITE('{pSettingsStr}','QphProCfg.ini','{esbn}','pSettings{compilerversion}')
ELSE
	FILEWRITE('success','pSettings.txt','{pSettingsStr}')
ENDIF