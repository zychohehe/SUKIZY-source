package com.metrolist.android.ui.screens.settings

import androidx.compose.runtime.Composable

@Composable
fun PlaylistImportScreen(
    onNavigateBack: () -> Unit,
    openSpotifyOnStart: Boolean = false,
) {
    BackupRestoreScreen(onNavigateBack = onNavigateBack, openSpotifyOnStart = openSpotifyOnStart)
}
