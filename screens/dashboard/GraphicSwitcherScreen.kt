package org.babetech.borastock.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DonutLarge
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters
import com.aay.compose.baseComponents.model.GridOrientation
import com.aay.compose.donutChart.DonutChart
import com.aay.compose.donutChart.PieChart
import com.aay.compose.donutChart.model.PieChartData
import com.aay.compose.lineChart.LineChart
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.lineChart.model.LineType
import com.aay.compose.radarChart.RadarChart
import com.aay.compose.radarChart.model.NetLinesStyle
import com.aay.compose.radarChart.model.Polygon
import com.aay.compose.radarChart.model.PolygonStyle

data class ChartType(
    val name: String,
    val displayName: String,
    val icon: ImageVector,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphicSwitcherScreen() {
    var selectedChart by remember { mutableStateOf("Line") }

    val chartTypes = listOf(
        ChartType("Line", "Courbes", Icons.Default.ShowChart, "Évolution temporelle"),
        ChartType("Bar", "Barres", Icons.Default.BarChart, "Comparaisons"),
        ChartType("Pie", "Secteurs", Icons.Default.PieChart, "Répartitions"),
        ChartType("Donut", "Anneau", Icons.Default.DonutLarge, "Proportions"),
        ChartType("Radar", "Radar", Icons.Default.Analytics, "Multi-critères")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        // Header avec titre
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(20.dp),
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Analyses & Graphiques",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Visualisez vos données avec différents types de graphiques",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Sélecteur de graphiques moderne
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Type de graphique",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    chartTypes.forEach { chartType ->
                        ChartTypeButton(
                            chartType = chartType,
                            isSelected = selectedChart == chartType.name,
                            onClick = { selectedChart = chartType.name },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Graphique sélectionné
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(20.dp)
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                val currentChart = chartTypes.find { it.name == selectedChart }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = currentChart?.icon ?: Icons.Default.Analytics,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    Column {
                        Text(
                            text = currentChart?.displayName ?: "Graphique",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = currentChart?.description ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.3f))
                        .padding(16.dp)
                ) {
                    when (selectedChart) {
                        "Line" -> GraphicScreen()
                        "Bar" -> BarChartSample()
                        "Pie" -> PieChartSample()
                        "Donut" -> DonutChartSample()
                        "Radar" -> RadarChartSample()
                    }
                }
            }
        }
    }
}

@Composable
fun ChartTypeButton(
    chartType: ChartType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = chartType.icon,
                contentDescription = null,
                tint = if (isSelected) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = chartType.displayName,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = if (isSelected) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun GraphicScreen() {
    val testLineParameters: List<LineParameters> = listOf(
        LineParameters(
            label = "Revenus",
            data = listOf(70.0, 85.0, 50.33, 40.0, 100.500, 50.0),
            lineColor = MaterialTheme.colorScheme.primary,
            lineType = LineType.CURVED_LINE,
            lineShadow = true,
        ),
        LineParameters(
            label = "Bénéfices",
            data = listOf(60.0, 80.6, 40.33, 86.232, 88.0, 90.0),
            lineColor = MaterialTheme.colorScheme.secondary,
            lineType = LineType.DEFAULT_LINE,
            lineShadow = true
        ),
        LineParameters(
            label = "Coûts",
            data = listOf(30.0, 40.0, 35.33, 55.23, 45.0, 60.0),
            lineColor = MaterialTheme.colorScheme.tertiary,
            lineType = LineType.CURVED_LINE,
            lineShadow = false,
        )
    )

    LineChart(
        modifier = Modifier.fillMaxSize(),
        linesParameters = testLineParameters,
        isGrid = true,
        gridColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
        xAxisData = listOf("Jan", "Fév", "Mar", "Avr", "Mai", "Jun"),
        animateChart = true,
        showGridWithSpacer = true,
        yAxisStyle = TextStyle(
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        ),
        xAxisStyle = TextStyle(
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        ),
        yAxisRange = 14,
        oneLineChart = false,
        gridOrientation = GridOrientation.VERTICAL
    )
}

@Composable
fun BarChartSample() {
    val testBarParameters: List<BarParameters> = listOf(
        BarParameters(
            dataName = "Produits A",
            data = listOf(60.6, 70.6, 80.0, 50.6, 44.0, 90.6, 65.0),
            barColor = MaterialTheme.colorScheme.primary
        ),
        BarParameters(
            dataName = "Produits B",
            data = listOf(50.0, 30.6, 77.0, 69.6, 50.0, 30.6, 80.0),
            barColor = MaterialTheme.colorScheme.secondary,
        ),
        BarParameters(
            dataName = "Produits C",
            data = listOf(40.0, 55.6, 60.0, 80.6, 35.0, 70.6, 55.99),
            barColor = MaterialTheme.colorScheme.tertiary,
        ),
    )

    BarChart(
        chartParameters = testBarParameters,
        gridColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
        xAxisData = listOf("Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"),
        isShowGrid = true,
        animateChart = true,
        showGridWithSpacer = true,
        yAxisStyle = TextStyle(
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        ),
        xAxisStyle = TextStyle(
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        ),
        yAxisRange = 15,
        barWidth = 20.dp
    )
}

@Composable
fun PieChartSample() {
    val testPieChartData: List<PieChartData> = listOf(
        PieChartData(
            partName = "Électronique",
            data = 500.0,
            color = MaterialTheme.colorScheme.primary,
        ),
        PieChartData(
            partName = "Vêtements",
            data = 700.0,
            color = MaterialTheme.colorScheme.secondary,
        ),
        PieChartData(
            partName = "Alimentation",
            data = 500.0,
            color = MaterialTheme.colorScheme.tertiary,
        ),
        PieChartData(
            partName = "Autres",
            data = 300.0,
            color = MaterialTheme.colorScheme.error,
        ),
    )

    PieChart(
        modifier = Modifier.fillMaxSize(),
        pieChartData = testPieChartData,
        ratioLineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
        textRatioStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        ),
    )
}

@Composable
fun DonutChartSample() {
    val testPieChartData: List<PieChartData> = listOf(
        PieChartData(
            partName = "Stock A",
            data = 500.0,
            color = MaterialTheme.colorScheme.primary,
        ),
        PieChartData(
            partName = "Stock B",
            data = 700.0,
            color = MaterialTheme.colorScheme.secondary,
        ),
        PieChartData(
            partName = "Stock C",
            data = 500.0,
            color = MaterialTheme.colorScheme.tertiary,
        ),
        PieChartData(
            partName = "Stock D",
            data = 300.0,
            color = MaterialTheme.colorScheme.error,
        ),
    )

    DonutChart(
        modifier = Modifier.fillMaxSize(),
        pieChartData = testPieChartData,
        centerTitle = "Stock Total",
        centerTitleStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        ),
        outerCircularColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
        innerCircularColor = MaterialTheme.colorScheme.surfaceContainer,
        ratioLineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
    )
}

@Composable
fun RadarChartSample() {
    val radarLabels = listOf(
        "Qualité",
        "Prix",
        "Service",
        "Rapidité",
        "Innovation",
        "Support"
    )
    val values1 = listOf(180.0, 160.0, 165.0, 135.0, 120.0, 150.0)
    val values2 = listOf(120.0, 140.0, 110.0, 112.0, 180.0, 120.0)
    
    val labelsStyle = TextStyle(
        color = MaterialTheme.colorScheme.onSurface,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )

    val scalarValuesStyle = TextStyle(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp
    )

    RadarChart(
        modifier = Modifier.fillMaxSize(),
        radarLabels = radarLabels,
        labelsStyle = labelsStyle,
        netLinesStyle = NetLinesStyle(
            netLineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            netLinesStrokeWidth = 1.5f,
            netLinesStrokeCap = StrokeCap.Round
        ),
        scalarSteps = 4,
        scalarValue = 200.0,
        scalarValuesStyle = scalarValuesStyle,
        polygons = listOf(
            Polygon(
                values = values1,
                unit = "%",
                style = PolygonStyle(
                    fillColor = MaterialTheme.colorScheme.primary,
                    fillColorAlpha = 0.3f,
                    borderColor = MaterialTheme.colorScheme.primary,
                    borderColorAlpha = 0.8f,
                    borderStrokeWidth = 2f,
                    borderStrokeCap = StrokeCap.Round,
                )
            ),
            Polygon(
                values = values2,
                unit = "%",
                style = PolygonStyle(
                    fillColor = MaterialTheme.colorScheme.secondary,
                    fillColorAlpha = 0.3f,
                    borderColor = MaterialTheme.colorScheme.secondary,
                    borderColorAlpha = 0.8f,
                    borderStrokeWidth = 2f,
                    borderStrokeCap = StrokeCap.Round
                )
            )
        )
    )
}