package ru.ibs.transform.xml.services

import org.apache.xml.security.c14n.Canonicalizer
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import kotlinx.serialization.json.Json

@Service
class CanonicalService {

    private val json = Json {
        prettyPrint = false
    }

    fun canonicalizeXml(xmlString: String): String =
        ByteArrayOutputStream().use {
            Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_OMIT_COMMENTS)
                .canonicalize(xmlString.toByteArray(Charsets.UTF_8), it, false)
            String(bytes = it.toByteArray(), charset = Charsets.UTF_8)
        }

    fun canonicalizeJson(jsonMap: Map<String, String>) : String = json.encodeToString(LinkedHashMap(jsonMap.toSortedMap()))

}
