package com.example.rp_android.connection;

/**
 * Soubor prihlasuje uzivatele do Pusher
 *
 */
public class PusherConnectionFile {
    /**
     * Prihlasovaci verejny klic napr: 372c7499d55d1cd7cd8b
     */
        static String key = "372c7499d55d1cd7cd8b";

        /** Prohlasovaci cluster, pr: eu, en */
        static String cluster = "eu";

        public static String returnKey() {
            return key;
        }

        public static String returnCluster() {
            return cluster;
        }
    }
