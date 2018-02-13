package com.bogovich.xml.writer.dsl

import com.sun.xml.txw2.output.IndentingXMLStreamWriter
import mu.KLogging
import javax.xml.stream.XMLStreamWriter



class CoroutineXMLStreamWriter(writer: XMLStreamWriter?) : IndentingXMLStreamWriter(writer) {
    companion object : KLogging()

    suspend fun document(init: xmlStreamCoroutine): CoroutineXMLStreamWriter {
        this.writeStartDocument()
        this.init()
        this.writeEndDocument()
        this.flush()
        return this
    }

    suspend fun element(name: String, init: xmlStreamCoroutine): CoroutineXMLStreamWriter {
        this.writeStartElement(name)
        this.init()
        this.writeEndElement()
        return this
    }

    suspend fun element(namespace: String, name: String, init: xmlStreamCoroutine): CoroutineXMLStreamWriter {
        this.writeStartElement(namespace, name)
        this.init()
        this.writeEndElement()
        return this
    }

    suspend fun defaultNamespace(namespace: String): CoroutineXMLStreamWriter {
        this.writeDefaultNamespace(namespace)
        return this
    }

    suspend fun namespace(prefix: String, namespace: String): CoroutineXMLStreamWriter {
        this.writeNamespace(prefix, namespace)
        return this
    }

    suspend fun element(name: String, content: Any) {
        element(name) {
            writeCharacters(content.toString())
        }
    }

    suspend fun element(namespace: String, name: String, content: Any) {
        element(namespace, name) {
            writeCharacters(content.toString())
        }
    }

    fun attribute(name: String, value: Any) = writeAttribute(name, value.toString())

    suspend infix fun String.tag(value: Any) {
        element(this, value)
    }

    suspend infix fun String.tag(lambda: xmlStreamCoroutine) {
        element(this, lambda)
    }

    suspend infix fun Pair<String, String>.tag(value: Any) {
        element(this.first, this.second, value)
    }

    suspend infix fun Pair<String, String>.tag(lambda: xmlStreamCoroutine) {
        element(this.first, this.second, lambda)
    }

    suspend infix fun String.attr(value: Any) {
        attribute(this, value)
    }
}