package ru.tbank.education.school.lesson10.homework
import javax.print.Doc
import kotlin.reflect.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties

object DocumentationGenerator {
    fun generateDoc(obj: Any): String {
        var res = mutableListOf<String>()
        var hiddenParams = mutableListOf<String>()
        val internalApi = obj::class.findAnnotation<InternalApi>()
        if (internalApi != null) {
            res.add("Документация скрыта (InternalApi).")
            return res.joinToString("\n")
        }

        val docs = obj::class.findAnnotation<DocClass>()
        if (docs != null) {
            res.add("=== Документация: ${obj::class.simpleName} ===")
                res.add("Описание: ${docs.description}")
                res.add("Автор: ${docs.author}")
                res.add("Версия: ${docs.version}")
        } else {
            res.add("Нет документации для класса.")
            return res.joinToString("\n")
        }


        obj::class.memberProperties.forEach { prop ->
            try {
                if (prop.findAnnotation<InternalApi>() != null) {
                    res.add("Свойство скрыто")
                    hiddenParams.add(prop.name)
                    return@forEach
                }

                val properties = obj::class.memberProperties.filter { prop -> prop.findAnnotation<InternalApi>() == null }
                if (properties.isNotEmpty()) {
                    res.add("--- Свойства ---")
                    properties.forEach { prop ->
                        res.add("- ${prop.name}")
                        prop.findAnnotation<DocProperty>()?.let { doc ->
                            res.add("  Описание: ${doc.description}")
                            if (doc.example.isNotEmpty()) {
                                res.add("  Пример: ${doc.example}")
                            }
                        }
                    }
                }
            } catch (e: Exception) {

            }
        }


        val methods = obj::class.memberFunctions.filterNot { method ->
            method.name in setOf("equals", "hashCode", "toString", "copy", "component1", "component1()", "component2()", "component2") ||
                    method.findAnnotation<InternalApi>() != null
        }
        if (methods.isNotEmpty()) {
            res.add("--- Методы ---")
            methods.forEach { method ->
                res.add("- ${method.name}")

                val methodAnnotation = method.findAnnotation<DocMethod>()
                if (methodAnnotation != null) {
                    res.add("   Описание: ${methodAnnotation.description}")
                    val parameter = method.parameters.filter { it.name != null }
                    if (parameter.isNotEmpty()) {
                        res.add("   Параметры:")
                        parameter.forEach { param ->
                            if (param.name != null) {
                                res.add("   - ${param.name}: ${param.findAnnotation<DocParam>()?.description ?: ""}")
                                res.add("Возвращает: ${methodAnnotation.returns}")
                            }
                        }
                    }
                } else {
                    val parameters = method.parameters.filter { it.name != null && it.index > 0 && !hiddenParams.contains(it.name) }
                    if (parameters.isNotEmpty()) {
                        res.add("   Параметры:")
                        parameters.forEach { param ->
                            res.add("   - ${param.name}: Нет описания")
                        }
                    }
                    res.add("   Возвращает: Нет описания")
                }
            }
        }


        return res.joinToString("\n")
    }
}