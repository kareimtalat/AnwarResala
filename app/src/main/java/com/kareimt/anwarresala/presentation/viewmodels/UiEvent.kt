package com.kareimt.anwarresala.presentation.viewmodels

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
}