package com.github.barrettotte.dsl5250.constant

// Groovy version of 
//   https://github.com/terminaldriver/terminaldriver/blob/master/src/main/java/com/terminaldriver/tn5250j/obj/Key.java

enum Key{
    ATTENTION('[attn]'),
	SYSTEM_REQUEST('[sysreq]'),
	RESET('[reset]'),
	CLEAR('[clear]'),
	HELP('[help]'),
	PG_UP('[pgup]'),
	PG_DOWN('[pgdown]'),
	ROLL_LEFT('[rollleft]'),
	ROLL_RIGHT('[rollright]'),
	FIELD_EXIT('[fldext]'),
	FIELD_PLUS('[field+]'),
	FIELD_MINUS('[field-]'),
	BOF('[bof]'),
	ENTER('[enter]'),
	HOME('[home]'),
	INSERT('[insert]'),
	BACKSPACE('[backspace]'),
	BACKTAB('[backtab]'),
	UP('[up]'),
	DOWN('[down]'),
	LEFT('[left]'),
	RIGHT('[right]'),
	DELETE('[delete]'),
	TAB('[tab]'),
	EOF('[eof]'),
	ERASE_EOF('[eraseeof]'),
	ERASE_FIELD('[erasefld]')

    String code
    Key(final String code){
        this.code= code
    }
}