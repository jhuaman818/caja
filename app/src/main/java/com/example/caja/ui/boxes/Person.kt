package com.example.caja.ui.boxes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.example.caja.models.Persons
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.window.Dialog
import com.example.caja.ViewModel.TablesViewModel
import com.example.caja.models.IdentityDocumentType
import androidx.compose.runtime.LaunchedEffect
import com.example.caja.models.Country
import com.example.caja.models.Department
import com.example.caja.models.District
import com.example.caja.models.Province

@Composable
fun NewPersonForm(
    tablesViewModel: TablesViewModel,
    countries: List<Country>,
    departments: List<Department>,
    provinces: List<Province>,
    districts: List<District>,
    identityDocumentTypes: List<IdentityDocumentType>,

    onGuardar: (Persons) -> Unit, onCancelar: () -> Unit) {

    LaunchedEffect(Unit) {
        tablesViewModel.fetchTables()
    }


    Dialog(onDismissRequest = onCancelar) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp
        ) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(scrollState)
                    .widthIn(max = 400.dp)
            ) {
                var number by remember { mutableStateOf("") }
                var name by remember { mutableStateOf("") }

                var tradeName by remember { mutableStateOf("") }
                var countryId by remember { mutableStateOf("") }
                var departmentId by remember { mutableStateOf("") }
                var provinceId by remember { mutableStateOf("") }
                var districtId by remember { mutableStateOf("") }
                var address by remember { mutableStateOf("") }
                var email by remember { mutableStateOf("") }
                var telephone by remember { mutableStateOf("") }
                var identityDocumentTypeId by remember { mutableStateOf("") }

                //OutlinedTextField(value = identityDocumentTypeId, onValueChange = { identityDocumentTypeId = it }, label = { Text("Tipo Doc. Identidad") }, modifier = Modifier.fillMaxWidth())
                IdentityDocumentTypeDropdown(
                    identityDocumentTypes = identityDocumentTypes,
                    selectedType = identityDocumentTypeId,
                    onTypeSelected = { identityDocumentTypeId = it }
                )
                OutlinedTextField(value = number, onValueChange = { number = it }, label = { Text("Número identificacion") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre completo") }, modifier = Modifier.fillMaxWidth())

                OutlinedTextField(value = tradeName, onValueChange = { tradeName = it }, label = { Text("Nombre Comercial") }, modifier = Modifier.fillMaxWidth())
                //OutlinedTextField(value = countryId, onValueChange = { countryId = it }, label = { Text("País") }, modifier = Modifier.fillMaxWidth())
                CountryDropdown(
                    countries = countries,
                    selectedCountry = countryId,
                    onCountrySelected = { countryId = it }
                )

                //OutlinedTextField(value = departmentId, onValueChange = { departmentId = it }, label = { Text("Departamento") }, modifier = Modifier.fillMaxWidth())
                DepartmentDropdown(
                    departments = departments,
                    selectedDepartment = departmentId,
                    onDepartmentSelected = { departmentId = it }
                )
                //OutlinedTextField(value = provinceId, onValueChange = { provinceId = it }, label = { Text("Provincia") }, modifier = Modifier.fillMaxWidth())
                ProvinceDropdown(
                    provinces = provinces,
                    selectedProvince = provinceId,
                    onProvinceSelected = { provinceId = it }
                )
                //OutlinedTextField(value = districtId, onValueChange = { districtId = it }, label = { Text("Distrito") }, modifier = Modifier.fillMaxWidth())
                DistrictDropdown(
                    districts = districts,
                    selectedDistrict = districtId,
                    onDistrictSelected = { districtId = it }
                )
                OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = telephone, onValueChange = { telephone = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())

                // Copia el contenido de tu formulario aquí
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    val persons = Persons(
                        id = 0, // Assuming new person has no ID yet
                        number = number,
                        name = name,
                        trade_name = tradeName,
                        country_id = countryId,
                        department_id = departmentId.toIntOrNull() ?: 0,
                        province_id = provinceId.toIntOrNull(),
                        district_id = districtId,
                        address = address,
                        email = email,
                        telephone = telephone,
                        identity_document_type_id = identityDocumentTypeId,
                        updated_at = ""
                    )
                    onGuardar(persons)
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Guardar")
                }
            }
        }
    }
}
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun CountryDropdown(
    countries: List<Country>,
    selectedCountry: String,
    onCountrySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = countries.find { it.id.toString() == selectedCountry }?.description ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("País") },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            countries.forEach { country ->
                DropdownMenuItem(
                    text = { Text(country.description) },
                    onClick = {
                        onCountrySelected(country.id.toString())
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepartmentDropdown(
    departments: List<Department>,
    selectedDepartment: String,
    onDepartmentSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = departments.find { it.id.toString() == selectedDepartment }?.description ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Departamento") },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            departments.forEach { department ->
                DropdownMenuItem(
                    text = { Text(department.description) },
                    onClick = {
                        onDepartmentSelected(department.id.toString())
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProvinceDropdown(
    provinces: List<Province>,
    selectedProvince: String,
    onProvinceSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = provinces.find { it.id.toString() == selectedProvince }?.description ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Provincia") },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            provinces.forEach { province ->
                DropdownMenuItem(
                    text = { Text(province.description) },
                    onClick = {
                        onProvinceSelected(province.id.toString())
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistrictDropdown(
    districts: List<District>,
    selectedDistrict: String,
    onDistrictSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = districts.find { it.id.toString() == selectedDistrict }?.description ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Distrito") },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            districts.forEach { district ->
                DropdownMenuItem(
                    text = { Text(district.description) },
                    onClick = {
                        onDistrictSelected(district.id.toString())
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdentityDocumentTypeDropdown(
    identityDocumentTypes: List<IdentityDocumentType>,
    selectedType: String,
    onTypeSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = identityDocumentTypes.find { it.id.toString() == selectedType }?.description ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Tipo Doc. Identidad") },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            identityDocumentTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.description) },
                    onClick = {
                        onTypeSelected(type.id.toString())
                        expanded = false
                    }
                )
            }
        }
    }
}
