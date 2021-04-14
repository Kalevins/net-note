package models

data class Document(
    val title:String,
    val chapters:Int,
    val audio:Boolean,
    val scans:Boolean,
    val texts:Boolean,
    val content: String

) {
}