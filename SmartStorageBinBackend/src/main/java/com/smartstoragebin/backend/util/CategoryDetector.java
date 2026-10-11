package com.smartstoragebin.backend.util;

import com.smartstoragebin.backend.entity.enums.CategoryType;
import java.util.HashMap;
import java.util.Map;

public class CategoryDetector {

    private static final Map<String, CategoryType> EXTENSION_MAP = new HashMap<>();

    static {
        // Images
        EXTENSION_MAP.put("jpg", CategoryType.IMAGES);
        EXTENSION_MAP.put("jpeg", CategoryType.IMAGES);
        EXTENSION_MAP.put("png", CategoryType.IMAGES);
        EXTENSION_MAP.put("gif", CategoryType.IMAGES);
        EXTENSION_MAP.put("bmp", CategoryType.IMAGES);
        EXTENSION_MAP.put("webp", CategoryType.IMAGES);

        // Videos
        EXTENSION_MAP.put("mp4", CategoryType.VIDEOS);
        EXTENSION_MAP.put("avi", CategoryType.VIDEOS);
        EXTENSION_MAP.put("mkv", CategoryType.VIDEOS);
        EXTENSION_MAP.put("mov", CategoryType.VIDEOS);
        EXTENSION_MAP.put("wmv", CategoryType.VIDEOS);

        // Documents
        EXTENSION_MAP.put("pdf", CategoryType.DOCUMENTS);
        EXTENSION_MAP.put("doc", CategoryType.DOCUMENTS);
        EXTENSION_MAP.put("docx", CategoryType.DOCUMENTS);
        EXTENSION_MAP.put("txt", CategoryType.DOCUMENTS);
        EXTENSION_MAP.put("rtf", CategoryType.DOCUMENTS);

        // Downloads
        EXTENSION_MAP.put("zip", CategoryType.DOWNLOADS);
        EXTENSION_MAP.put("rar", CategoryType.DOWNLOADS);
        EXTENSION_MAP.put("tar", CategoryType.DOWNLOADS);
        EXTENSION_MAP.put("gz", CategoryType.DOWNLOADS);
        EXTENSION_MAP.put("apk", CategoryType.DOWNLOADS);
    }

    public static CategoryType detectCategory(String fileName, String fileType) {
        if (fileName == null) {
            return CategoryType.OTHER;
        }
        
        String extension = detectFileType(fileName).toLowerCase();
        
        if (EXTENSION_MAP.containsKey(extension)) {
            return EXTENSION_MAP.get(extension);
        }
        
        if (fileType != null && !fileType.isEmpty()) {
             if (fileType.startsWith("image/")) return CategoryType.IMAGES;
             if (fileType.startsWith("video/")) return CategoryType.VIDEOS;
             if (fileType.startsWith("text/") || fileType.equals("application/pdf")) return CategoryType.DOCUMENTS;
        }

        return CategoryType.OTHER;
    }

    public static String getCategoryIcon(CategoryType category) {
        if (category == null) return "file-outline";
        switch (category) {
            case IMAGES: return "image";
            case VIDEOS: return "video";
            case DOCUMENTS: return "file-document";
            case DOWNLOADS: return "download";
            case AUDIO: return "music";
            case OTHER:
            default: return "file-outline";
        }
    }

    public static String detectFileType(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
    }
}
