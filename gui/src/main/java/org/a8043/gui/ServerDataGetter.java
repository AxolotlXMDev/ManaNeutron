package org.a8043.gui;

import github.axolotl.ai.session.Session;
import github.axolotl.ai.session.SessionInfo;
import github.axolotl.ai.session.SessionInfos;
import github.axolotl.setting.Settings;

import java.util.List;
import java.util.stream.Collectors;

public class ServerDataGetter {
        private static Settings settings;
        private static SessionInfos sessionInfos;
        private static SessionInfo lastSessionInfo;

        public static Settings getSettings() {
                return settings != null ? settings :
                        (settings = Main.instance.getCurrentClient().getSettings());
        }

        public static SessionInfos getSessionInfos() {
                return sessionInfos != null ? sessionInfos :
                        (sessionInfos = Main.instance.getCurrentClient().getSessions());
        }

        public static SessionInfo getLastSessionInfo() {
                return lastSessionInfo != null ? lastSessionInfo :
                        (lastSessionInfo = Main.instance.getCurrentClient().getLastSessionInfo());
        }

        public static Session getSessionById(String id) {
                return Main.instance.getCurrentClient().getSessionById(id);
        }

        public static List<Model> getModels() {
                return getSettings().getProviders().stream()
                        .flatMap(p -> p.getModelIds().stream().map(m -> new Model(p, m)))
                        .collect(Collectors.toList());
        }

        public static void cleanCache() {
                settings = null;
                sessionInfos = null;
                lastSessionInfo = null;
        }
}
