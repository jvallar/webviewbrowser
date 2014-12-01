;
; tmp directory (this should only occur for non-Admin XP users)
;
FILEREAD('tmpFileExists','pSettingsStr','pSettings.txt')
IF({tmpFileExists})
	SET('cfgSaveTo','tmp')
;
; ini directory and tests
;
ELSE
	;
	; read ini
	;
	INIREAD('pSettingsStr','QphProCfg.ini','{esbn}','pSettings{compilerversion}')

	;
	; tests
	;
	INIWRITE('iniok','QphProCfg.ini','{esbn}','iniTestStr{compilerversion}')
	INIREAD('iniTestOK','QphProCfg.ini','{esbn}','iniTestStr{compilerversion}')
	IFSTRNOTEQUAL('{iniTestOK}','')
		SET('cfgSaveTo','ini')
		INIWRITE('','QphProCfg.ini','{esbn}','iniTestStr{compilerversion}')
	ELSE
		SET('cfgSaveTo','tmp')
	ENDIF
ENDIF