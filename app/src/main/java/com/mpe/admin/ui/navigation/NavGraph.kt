package com.mpe.admin.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.mpe.admin.ui.screens.*
import com.mpe.admin.ui.theme.Gold
import com.mpe.admin.ui.theme.ObsidianLight
import com.mpe.admin.ui.theme.TextSecondary
import com.mpe.admin.viewmodel.AdminViewModel

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Login : Screen("login", "Connexion", Icons.Default.Lock)
    object Dashboard : Screen("dashboard", "Accueil", Icons.Default.Home)
    object Villas : Screen("villas", "Villas", Icons.Default.House)
    object Apartments : Screen("apartments", "Apparts", Icons.Default.Apartment)
    object Cars : Screen("cars", "Véhicules", Icons.Default.DirectionsCar)
    object Settings : Screen("settings", "Paramètres", Icons.Default.Settings)
}

private val bottomNavItems = listOf(
    Screen.Dashboard,
    Screen.Villas,
    Screen.Apartments,
    Screen.Cars,
    Screen.Settings
)

@Composable
fun NavGraph(viewModel: AdminViewModel) {
    val navController = rememberNavController()
    val isAuthenticated by viewModel.isAuthenticated.collectAsStateWithLifecycle()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    LaunchedEffect(isAuthenticated) {
        if (!isAuthenticated) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        } else if (currentRoute == Screen.Login.route || currentRoute == null) {
            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    val showBottomBar = isAuthenticated && currentRoute != Screen.Login.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = ObsidianLight) {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.label) },
                            label = {
                                Text(
                                    text = screen.label,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Gold,
                                selectedTextColor = Gold,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = ObsidianLight
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isAuthenticated) Screen.Dashboard.route else Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) { LoginScreen(viewModel) }
            composable(Screen.Dashboard.route) { DashboardScreen(viewModel) }
            composable(Screen.Villas.route) { PropertiesScreen("villas", viewModel) }
            composable(Screen.Apartments.route) { PropertiesScreen("apartments", viewModel) }
            composable(Screen.Cars.route) { CarsScreen(viewModel) }
            composable(Screen.Settings.route) { SettingsScreen(viewModel) }
        }
    }
}
