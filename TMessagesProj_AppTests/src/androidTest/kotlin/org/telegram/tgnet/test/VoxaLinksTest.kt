package org.telegram.tgnet.test

import android.net.Uri
import org.junit.Assert.*
import org.junit.Test
import org.telegram.messenger.VoxaLinks
import org.telegram.utils.proxy.ProxySettings

class VoxaLinksTest {
    @Test
    fun serverLinksKeepTheirPathAndParameters() {
        val links = mapOf(
            "https://t.me/channel/123?thread=4#part" to "https://voxa.ir/channel/123?thread=4#part",
            "https://telesrv.net/+invite%2Btoken" to "https://voxa.ir/+invite%2Btoken",
            "https://t.me/boost/?c=123" to "https://voxa.ir/boost/?c=123",
            "https://t.me/giftcode/abc" to "https://voxa.ir/giftcode/abc",
            "http://telegram.me/c/123/45" to "https://voxa.ir/c/123/45"
        )
        links.forEach { (input, expected) -> assertEquals(expected, VoxaLinks.toVoxaLink(input)) }
    }

    @Test
    fun unrelatedOrDeceptiveUrlsAreNotRewritten() {
        listOf("https://t.me.evil.example/channel", "https://t.me@evil.example/channel",
            "https://example.com/t.me/channel", "tg://resolve?domain=channel", "not a url")
            .forEach { assertEquals(it, VoxaLinks.toVoxaLink(it)) }
        assertNull(VoxaLinks.toVoxaLink(null))
        assertTrue(VoxaLinks.isLinkHost("VOXA.IR"))
        assertTrue(VoxaLinks.isLinkHost("telesrv.net"))
        assertFalse(VoxaLinks.isLinkHost("voxa.ir.evil.example"))
    }

    @Test
    fun brandedAndLegacyProxyLinksCanBeParsed() {
        assertNotNull(ProxySettings.fromUri(Uri.parse("https://voxa.ir/socks?server=example.com&port=1080")))
        assertNotNull(ProxySettings.fromUri(Uri.parse("https://telesrv.net/socks?server=example.com&port=1080")))
        assertNull(ProxySettings.fromUri(Uri.parse("https://voxa.ir.evil.example/socks?server=example.com&port=1080")))
    }
}
