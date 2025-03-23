package com.perry.utilslib

import android.content.Context
import android.os.Environment
import java.io.File

/**
 * @Author perry
 * @Date 2025/3/23 14:56
 * @Description
 * @Version 1.0
 **/
object FileUtils {
    /**
     * 往路径：/data/data/<package-name>/files/ 写入内容
     * @param filename 文件名，不需要绝对路径，默认就是往上述路径中写入
     */
    @JvmStatic
    fun writeToInternalStorage(context: Context?, filename: String?, content: String?) {
        if (context == null || filename == null || content == null) return
        context.openFileOutput(filename, Context.MODE_PRIVATE).use { fos ->
            fos.write(content.toByteArray())
        }
    }

    /**
     * 1、路径：/sdcard/Documents/
     * 2、需声明 WRITE_EXTERNAL_STORAGE 权限
     */
    fun writeToExternalStorage(content: String, filename: String) {
        val dir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) ?: return
        val file = File(dir, filename)
        file.writeText(content) // Kotlin 扩展方法，自动处理 UTF-8
    }

    /**
     * 属于应用内部存储路径，用户不可见
     * 在 /data/data/<应用包名>/files/ 目前下创建dirName文件夹
     */
    fun createInternalDir(context: Context, dirName: String): File? {
        val internalDir = File(context.filesDir, dirName)
        var result = true
        if (!internalDir.exists()) {
            result = internalDir.mkdirs() // 递归创建多级目录
        }
        return if (result) internalDir else null
    }

    /**
     * 属于应用内部存储路径，用户不可见
     * 在 /data/data/<应用包名>/cache/ 目前下创建dirName文件夹
     */
    fun createCacheDir(context: Context, dirName: String): File? {
        val cacheDir = File(context.cacheDir, dirName)
        var result = true
        if (!cacheDir.exists()) {
            result = cacheDir.mkdirs()
        }
        return if (result) cacheDir else null
    }

    /**
     * 属于应用内部存储路径，用户不可见
     * 创建 /data/data/{包名}/app_dirName 目录（系统自动添加 app_ 前缀）
     */
    fun createDefaultDir(context: Context, dirName: String): File? {
        val defaultDir = context.getDir(dirName, Context.MODE_PRIVATE) ?: return null
        if (!defaultDir.exists()) {
            defaultDir.mkdirs()
        }
        return defaultDir
    }


    /**
     * 属于应用的外部存储路径，但不是SD卡，注意区别，该目录用户可见
     * 在 storage/emulated/0/Android/data/{包名}/files/ 目录下创建目录
     */
    fun createExternalDir(context: Context, dirName: String): File? {
        val externalDir = context.getExternalFilesDir(null) ?: return null
        val customDir = File(externalDir, dirName)
        customDir.mkdirs()
        return customDir
    }

    /**
     * 属于应用的外部存储路径，但不是SD卡，注意区别，该目录用户可见
     * 在 storage/emulated/0/Android/data/{包名}/cache/ 目录下创建目录
     */
    fun createExternalCacheDir(context: Context, dirName: String): File? {
        val externalDir = context.externalCacheDir ?: return null
        val customDir = File(externalDir, dirName)
        customDir.mkdirs()
        return customDir
    }

    /**
     * 获取文件大小
     */
    fun getFileSize(file: File): String {
        val sizeBytes = file.length()
        return when {
            sizeBytes >= 1_000_000 -> "${sizeBytes / 1_000_000} MB"
            sizeBytes >= 1_000 -> "${sizeBytes / 1_000} KB"
            else -> "$sizeBytes B"
        }
    }

    /**
     * 返回某个文件夹下的所有文件
     */
    fun listFiles(dir: File): List<File> {
        return dir.listFiles()?.filter { it.isFile } ?: emptyList()
    }

    /**
     * 删除某个文件或文件夹，文件夹内部文件也会一并删除
     */
    fun deleteFile(file: File): Boolean {
        var result = true
        if (file.isDirectory) {
            val files = file.listFiles() ?: return file.delete()
            for (f in files) {
                result = deleteFile(f)
            }
        }
        return result && file.delete()
    }
}