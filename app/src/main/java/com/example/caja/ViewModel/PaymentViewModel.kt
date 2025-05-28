package com.example.caja.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.caja.ui.boxes.ProductoVenta

class PaymentViewModel : ViewModel() {
    val itemSelect = mutableStateListOf<ProductoVenta>()

    fun agregarProducto(producto: ProductoVenta): Boolean {
        val index = itemSelect.indexOfFirst { it.nombre == producto.nombre }
        return if (index >= 0) {
            val existente = itemSelect[index]
            itemSelect[index] = existente.copy(
                cantidad = existente.cantidad + producto.cantidad,
                precio = existente.precio + producto.precio
            )
            false // Ya existía, solo sumó cantidad
        } else {
            itemSelect.add(producto)
            true // Se agregó nuevo
        }
    }

    fun eliminarProducto(index: Int) {
        if (index in itemSelect.indices) {
            itemSelect.removeAt(index)
        }
    }
}
