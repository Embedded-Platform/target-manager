package com.embeddedplatform.targetmanager.util;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Component
public class VersionUtil {
    public static String addVersion(String version, Integer[] addValues){
        Integer[] values = getValuesFromVersionTag(version);
        for (int i = 0; i < values.length; i++) {
            values[i] += addValues[i];
        }
        return getVersionTagFromValues(values);
    }
    private static String getVersionTagFromValues(Integer[] values) {
        StringJoiner version = new StringJoiner(".");
        for (Integer value : values) {
            version.add(String.valueOf(value));
        }
        return version.toString();
    }
    private static Integer[] getValuesFromVersionTag(String version) {
        return Arrays.stream(version.split("\\."))
                .map(Integer::valueOf)
                .toArray(Integer[]::new);
    }
    public static Optional<String> getLastTagVersion(List<String> versions){
        return versions.stream().reduce((v1, v2) -> compareVersions(v1, v2) > 0 ? v1 : v2);
    }
    public static boolean isVersionTag(String versionTag){
        return versionTag.matches("^\\d+(\\.\\d+){0,2}$");
    }

    private static int compareVersions(String v1, String v2) {

        Integer[] valuesV1 = getValuesFromVersionTag(v1);
        Integer[] valuesV2 = getValuesFromVersionTag(v2);

        int length = Math.max(valuesV1.length, valuesV2.length);

        for (int i = 0; i < length; i++) {
            int numPartV1 = i < valuesV1.length ? valuesV1[i] : 0;
            int numPartV2 = i < valuesV2.length ? valuesV2[i] : 0;

            if (numPartV1 != numPartV2) {
                return Integer.compare(numPartV1, numPartV2);
            }
        }

        return 0;
    }
}
