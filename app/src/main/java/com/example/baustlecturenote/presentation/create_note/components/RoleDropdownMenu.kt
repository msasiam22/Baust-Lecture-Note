package com.example.baustlecturenote.presentation.create_note.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.baustlecturenote.presentation.auth.components.CustomOutlinedTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleDropdownMenu(
    selectedRole: String,
    onRoleSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val roleOptions = listOf("CSE101", "SE502", "BGS201", "SE401", "CSE202")

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier.fillMaxWidth()
    ) {
        CustomOutlinedTextField(
            value = selectedRole,
            onValueChange = {},
            label = "Category",
            modifier = Modifier
                .width(200.dp)
                .menuAnchor(),
            trailingIcon = Icons.Default.ArrowDropDown
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            roleOptions.forEach { role ->
                DropdownMenuItem(
                    text = { Text(role) },
                    onClick = {
                        onRoleSelected(role)
                        isExpanded = false
                    }
                )
            }
        }
    }
}