package ru.tbank.education.school.homework

import java.io.EOFException
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.charset.StandardCharsets


/**
 * Интерфейс для подсчёта строк и слов в файле.
 */
interface FileAnalyzer {

    /**
     * Считает количество строк и слов в указанном входном файле и записывает результат в выходной файл.
     *
     * Словом считается последовательность символов, разделённая пробелами,
     * табуляциями или знаками перевода строки. Пустые части после разделения не считаются словами.
     *
     * @param inputFilePath путь к входному текстовому файлу
     * @param outputFilePath путь к выходному файлу, в который будет записан результат
     * @return true если операция успешна, иначе false
     */
    fun countLinesAndWordsInFile(inputFilePath: String, outputFilePath: String): Boolean
}


class IOFileAnalyzer : FileAnalyzer {
    override fun countLinesAndWordsInFile(inputFilePath: String, outputFilePath: String): Boolean {
        var cnt : Int = 0
        var cntwords : Int = 0
        try {
            val reader = File(inputFilePath).bufferedReader()
            var line : String?
            while (reader.readLine().also { line = it } != null) {
                cnt++
                val words = line?.split("\\s+".toRegex()) ?: emptyList()
                cntwords += words.size
            }
        }

        catch (e: FileNotFoundException) {
            println("Файл не найден: $inputFilePath")
            return false
        } catch (e : EOFException) {
            println("Неожиданный конец файла: $inputFilePath")
            return false
        } catch (e : IOException) {
            println("Неизвестное исключение: $inputFilePath")
            return false
        }

        try {
            val file = File(outputFilePath)
            file.writeText("Общее количество строк: ${cnt}\n" +
                    "Общее количество слов: ${cntwords}")
        } catch (e : IOException) {
            println("Ошибка записи в файл: $outputFilePath")
            return false
        }
        return true
    }
}


class NIOFileAnalyzer : FileAnalyzer {
    override fun countLinesAndWordsInFile(inputFilePath: String, outputFilePath: String): Boolean {
        var cnt : Int = 0
        var cntwords : Int = 0
        try {
            val reader = Files.readAllLines(Paths.get(inputFilePath))
            reader.forEach { line ->
                cnt++
                val words = line?.split("\\s+".toRegex()) ?: emptyList()
                cntwords += words.size
            }

        } catch (e: NoSuchFileException ) {
            println("Файл не найден: $inputFilePath")
            return false
        } catch (e : IOException) {
            println("Неизвестное исключение: $inputFilePath")
            return false
        }

        try {
            Files.write(Paths.get(outputFilePath), listOf(
                    "Общее количество строк: ${cnt}",
                    "Общее количество слов: ${cntwords}"
                )
            )
        } catch (e : IOException) {
            println("Ошибка записи в файл: $outputFilePath")
            return false
        }
        return true
    }
}

fun main() {
    val workdir = "C:\\Users\\Elena\\Documents\\projects\\school57kotlin3\\lesson5\\src\\main\\kotlin\\ru\\tbank\\education\\school\\homework\\"

    System.setOut(java.io.PrintStream(System.out, true, "UTF-8"))
    val files = IOFileAnalyzer()
    files.countLinesAndWordsInFile(workdir + "data.txt", workdir + "output.txt")
    val files2 = NIOFileAnalyzer()
    files2.countLinesAndWordsInFile(workdir + "data.txt", workdir + "output2.txt")
}