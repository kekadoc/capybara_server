package com.kekadoc.project.capybara.server.common.tools

import com.kekadoc.project.capybara.server.Server

object LoginGenerator {

    fun generate(surname: String, name: String, patronymic: String): String {
        return buildString {
            append(transliterate(surname))
            name.firstOrNull()?.let(::transliterate)?.also(::append)
            patronymic.firstOrNull()?.let(::transliterate)?.also(::append)
            append(Server.getTime().toString().takeLast(5))
        }
    }

    private val abcCyr = charArrayOf(' ', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', )
    private val abcLat = arrayOf(" ", "a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch", "sh", "sch", "", "i", "", "e", "ju", "ja", "A", "B", "V", "G", "D", "E", "E", "Zh", "Z", "I", "Y", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ts", "Ch", "Sh", "Sch", "", "I", "", "E", "Ju", "Ja")
    private val abcAlfabetUpper = charArrayOf('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z')
    private val abcAlfabetLower = charArrayOf('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z')

    fun transliterate(char: Char): String? {
        if (Character.UnicodeBlock.of(char) != Character.UnicodeBlock.CYRILLIC) return null
        val cyrIndex = abcCyr.indexOf(char)
        return abcLat.getOrNull(cyrIndex)
    }

    fun transliterate(text: String): String {
        return text.map {
            transliterate(it) ?: if (it.isLowerCase()) {
                abcAlfabetLower.random().toString()
            } else {
                abcAlfabetUpper.random().toString()
            }
        }.joinToString(separator = "")
    }

}