package org.telegram.messenger;

import java.net.URI;

/** Public links use Voxa's domain; server-side identifiers must remain unchanged. */
public final class VoxaLinks {
    public static final String HOST = "voxa.ir";

    private VoxaLinks() {}

    public static boolean isLinkHost(String host) {
        return host != null && (HOST.equalsIgnoreCase(host)
                || "telesrv.net".equalsIgnoreCase(host)
                || "t.me".equalsIgnoreCase(host)
                || "telegram.me".equalsIgnoreCase(host)
                || "telegram.dog".equalsIgnoreCase(host));
    }

    /** Rebrand known server-generated links only, preserving the path, query and fragment. */
    public static String toVoxaLink(String link) {
        if (link == null) return null;
        try {
            URI uri = new URI(link);
            String scheme = uri.getScheme();
            if (!("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme))
                    || !isLinkHost(uri.getHost()) || uri.getRawUserInfo() != null || uri.getPort() != -1) {
                return link;
            }
            StringBuilder result = new StringBuilder("https://").append(HOST);
            if (uri.getRawPath() != null) result.append(uri.getRawPath());
            if (uri.getRawQuery() != null) result.append('?').append(uri.getRawQuery());
            if (uri.getRawFragment() != null) result.append('#').append(uri.getRawFragment());
            return result.toString();
        } catch (Exception ignored) {
            return link;
        }
    }
}
