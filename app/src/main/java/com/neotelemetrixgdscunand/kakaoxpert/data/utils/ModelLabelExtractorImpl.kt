package com.neotelemetrixgdscunand.kakaoxpert.data.utils

import android.content.Context
import com.neotelemetrixgdscunand.kakaoxpert.domain.presentation.ModelLabelExtractor
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.metadata.MetadataExtractor
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject

@ViewModelScoped
class ModelLabelExtractorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ModelLabelExtractor {

    override fun extractNamesFromMetadata(modelPath: String): List<String> {
        val model = FileUtil.loadMappedFile(context, modelPath)
        try {
            val metadataExtractor = MetadataExtractor(model)
            val inputStream = metadataExtractor.getAssociatedFile("temp_meta.txt")
            val metadata =
                inputStream?.bufferedReader()?.use { it.readText() } ?: return emptyList()

            val regex = Regex("'names': \\{(.*?)\\}}", RegexOption.DOT_MATCHES_ALL)
            val match = regex.find(metadata)
            val namesContent = match?.groups?.get(1)?.value ?: return emptyList()

            val regex2 = Regex("\"([^\"]*)\"|'([^']*)'")
            val match2 = regex2.findAll(namesContent)
            val list = match2.map {
                it.groupValues[1].ifEmpty { it.groupValues[2] }
            }.toList()
            return list
        } catch (_: Exception) {
            return emptyList()
        } finally {
            model.clear()
        }
    }

    override fun extractNamesFromLabelFile(labelPath: String): List<String> {
        val labels = mutableListOf<String>()
        try {
            val inputStream: InputStream = context.assets.open(labelPath)
            val reader = BufferedReader(InputStreamReader(inputStream))

            var line: String? = reader.readLine()
            while (line != null && line != "") {
                labels.add(line)
                line = reader.readLine()
            }

            reader.close()
            inputStream.close()
            return labels
        } catch (e: IOException) {
            return emptyList()
        }
    }
}