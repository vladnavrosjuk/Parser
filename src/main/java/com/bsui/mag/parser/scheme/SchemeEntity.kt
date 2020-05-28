package com.bsui.mag.parser.scheme

class SchemeEntity {
    var name: String? = null
    var type: String? = null
    var parseAs: ParseAsEntity? = null

    override fun toString(): String {
        return "SchemeEntity{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", parseAs=" + parseAs +
                '}'
    }
}