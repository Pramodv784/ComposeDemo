package com.example.composedemo.utils

import android.R
import android.net.Uri
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.Colors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.content.ContextCompat
import java.util.Locale
import java.util.regex.Pattern


object LinkText {
    @Composable
    fun LinkifyText(text: String, modifier: Modifier = Modifier) {
        val uriHandler = LocalUriHandler.current
        val context = LocalContext.current
        val layoutResult = remember {
            mutableStateOf<TextLayoutResult?>(null)
        }
        val linksList = extractUrls(text)
        val annotatedString = buildAnnotatedString {
            append(text)
            linksList.forEach {
                addStyle(
                    style = SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    ),
                    start = it.start,
                    end = it.end
                )
                addStringAnnotation(
                    tag = "URL",
                    annotation = it.url,
                    start = it.start,
                    end = it.end
                )
            }
        }
        Text(
            text = annotatedString,
            style = MaterialTheme.typography.titleMedium,
            onTextLayout = { layoutResult.value = it },
            modifier = modifier
                .pointerInput(Unit) {
                    detectTapGestures { offsetPosition ->
                        layoutResult.value?.let {
                            val position = it.getOffsetForPosition(offsetPosition)
                            annotatedString.getStringAnnotations(position, position).firstOrNull()
                                ?.let { result ->
                                    if (result.tag == "URL") {
                                        val builder = CustomTabsIntent.Builder()
                                        builder.setToolbarColor(context.getColor(R.color.holo_blue_light))
                                        val customTabsIntent = builder.build()
                                        customTabsIntent.launchUrl(
                                            context,
                                            Uri.parse(result.item)
                                        )
                                    }
                                }
                        }
                    }
                }
        )
    }
    private val urlPattern: Pattern = Pattern.compile(
        "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
        Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL
    )

    fun extractUrls(text: String): List<LinkInfos> {
        val matcher = urlPattern.matcher(text)
        var matchStart: Int
        var matchEnd: Int
        val links = arrayListOf<LinkInfos>()

        while (matcher.find()) {
            matchStart = matcher.start(1)
            matchEnd = matcher.end()

            var url = text.substring(matchStart, matchEnd)
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "https://$url"

            links.add(LinkInfos(url, matchStart, matchEnd))
        }
        return links
    }

    fun String.capitalized(): String {
        return this.replaceFirstChar {
            if (it.isLowerCase())
                it.titlecase(Locale.getDefault())
            else it.toString()
        }
    }

    data class LinkInfos(
        val url: String,
        val start: Int,
        val end: Int
    )

}