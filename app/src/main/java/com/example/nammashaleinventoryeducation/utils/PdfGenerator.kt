package com.example.nammashaleinventoryeducation.utils

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.nammashaleinventoryeducation.data.entity.Asset
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object PdfGenerator {
    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842
    private const val MARGIN = 40f
    
    // Colors
    private val BLUE_PRIMARY = Color.parseColor("#2563EB")
    private val GREEN_SUCCESS = Color.parseColor("#16A34A")
    private val AMBER_WARNING = Color.parseColor("#D97706")
    private val RED_ERROR = Color.parseColor("#DC2626")
    private val GRAY_LIGHT = Color.parseColor("#F3F4F6")
    private val TEXT_DARK = Color.parseColor("#1F2937")
    private val TEXT_GRAY = Color.parseColor("#6B7280")

    fun generateAssetReport(
        context: Context,
        assets: List<Asset>,
        totalCount: Int,
        workingCount: Int,
        repairCount: Int,
        brokenCount: Int
    ): File? {
        return try {
            val document = PdfDocument()
            val paint = Paint()
            var pageNumber = 1
            
            var pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
            var page = document.startPage(pageInfo)
            var canvas = page.canvas
            var yPos = MARGIN

            // 1. Header
            drawHeader(canvas, paint)
            yPos += 80f

            // 2. Report Info
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.textSize = 10f
            paint.color = TEXT_GRAY
            val timeStamp = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
            canvas.drawText("Generated on: $timeStamp", MARGIN, yPos, paint)
            yPos += 30f

            // 3. Summary Section
            yPos = drawSummary(canvas, paint, yPos, totalCount, workingCount, repairCount, brokenCount)
            yPos += 40f

            // 4. Asset Table Header
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textSize = 14f
            paint.color = TEXT_DARK
            canvas.drawText("Inventory Details", MARGIN, yPos, paint)
            yPos += 20f
            
            yPos = drawTableHeader(canvas, paint, yPos)

            // 5. Asset Rows
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.textSize = 10f
            
            for (asset in assets) {
                // Check if we need a new page
                if (yPos > PAGE_HEIGHT - MARGIN - 40f) {
                    document.finishPage(page)
                    pageNumber++
                    pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
                    page = document.startPage(pageInfo)
                    canvas = page.canvas
                    yPos = MARGIN + 20f
                    // Redraw table header on new page
                    yPos = drawTableHeader(canvas, paint, yPos)
                }
                
                yPos = drawAssetRow(canvas, paint, yPos, asset)
            }

            // 6. Footer
            drawFooter(canvas, paint, pageNumber)

            document.finishPage(page)

            // Save the document
            val fileName = "NammaShaale_Report_${SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())}.pdf"
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = if (downloadsDir.exists() || downloadsDir.mkdirs()) {
                File(downloadsDir, fileName)
            } else {
                File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
            }

            val outputStream = FileOutputStream(file)
            document.writeTo(outputStream)
            outputStream.close()
            document.close()
            
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun drawHeader(canvas: Canvas, paint: Paint) {
        // Draw blue bar
        paint.color = BLUE_PRIMARY
        canvas.drawRect(MARGIN, MARGIN, PAGE_WIDTH - MARGIN, MARGIN + 60f, paint)
        
        // Draw Title
        paint.color = Color.WHITE
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 20f
        canvas.drawText("Namma-Shaale Inventory Report", MARGIN + 20f, MARGIN + 38f, paint)
        
        // Draw Subtitle
        paint.textSize = 10f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Smart School Asset Management System", MARGIN + 20f, MARGIN + 52f, paint)
    }

    private fun drawSummary(
        canvas: Canvas,
        paint: Paint,
        startY: Float,
        total: Int,
        working: Int,
        repair: Int,
        broken: Int
    ): Float {
        val cardWidth = (PAGE_WIDTH - (2 * MARGIN) - 30) / 4
        var xPos = MARGIN
        
        val stats = listOf(
            Triple("Total", total.toString(), BLUE_PRIMARY),
            Triple("Working", working.toString(), GREEN_SUCCESS),
            Triple("Repair", repair.toString(), AMBER_WARNING),
            Triple("Broken", broken.toString(), RED_ERROR)
        )
        
        for (stat in stats) {
            // Draw card background
            paint.color = GRAY_LIGHT
            canvas.drawRoundRect(xPos, startY, xPos + cardWidth, startY + 50f, 8f, 8f, paint)
            
            // Label
            paint.color = TEXT_GRAY
            paint.textSize = 9f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText(stat.first, xPos + 10f, startY + 18f, paint)
            
            // Value
            paint.color = stat.third
            paint.textSize = 16f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(stat.second, xPos + 10f, startY + 40f, paint)
            
            xPos += cardWidth + 10f
        }
        
        return startY + 60f
    }

    private fun drawTableHeader(canvas: Canvas, paint: Paint, y: Float): Float {
        paint.color = GRAY_LIGHT
        canvas.drawRect(MARGIN, y, PAGE_WIDTH - MARGIN, y + 25f, paint)
        
        paint.color = TEXT_DARK
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 10f
        
        canvas.drawText("Asset Name", MARGIN + 10f, y + 16f, paint)
        canvas.drawText("Serial Number", MARGIN + 180f, y + 16f, paint)
        canvas.drawText("Category", MARGIN + 340f, y + 16f, paint)
        canvas.drawText("Condition", MARGIN + 450f, y + 16f, paint)
        
        return y + 25f
    }

    private fun drawAssetRow(canvas: Canvas, paint: Paint, y: Float, asset: Asset): Float {
        paint.color = TEXT_DARK
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.textSize = 9f
        
        // Truncate text if too long
        val name = if (asset.assetName.length > 30) asset.assetName.take(27) + "..." else asset.assetName
        val serial = if (asset.serialNumber.length > 25) asset.serialNumber.take(22) + "..." else asset.serialNumber
        
        canvas.drawText(name, MARGIN + 10f, y + 18f, paint)
        canvas.drawText(serial, MARGIN + 180f, y + 18f, paint)
        canvas.drawText(asset.category, MARGIN + 340f, y + 18f, paint)
        
        // Status with color
        val statusColor = when (asset.condition) {
            Constants.CONDITION_WORKING -> GREEN_SUCCESS
            Constants.CONDITION_NEEDS_REPAIR -> AMBER_WARNING
            Constants.CONDITION_BROKEN -> RED_ERROR
            else -> TEXT_DARK
        }
        paint.color = statusColor
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText(asset.condition, MARGIN + 450f, y + 18f, paint)
        
        // Draw separator line
        paint.color = GRAY_LIGHT
        canvas.drawLine(MARGIN, y + 25f, PAGE_WIDTH - MARGIN, y + 25f, paint)
        
        return y + 25f
    }

    private fun drawFooter(canvas: Canvas, paint: Paint, pageNum: Int) {
        paint.color = TEXT_GRAY
        paint.textSize = 8f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        val footerText = "Generated by Namma-Shaale Inventory App | Page $pageNum"
        val textWidth = paint.measureText(footerText)
        canvas.drawText(footerText, (PAGE_WIDTH - textWidth) / 2, PAGE_HEIGHT - 20f, paint)
    }

    fun shareReport(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share Inventory Report"))
    }
}
