package com.example.owlfriend.presentation.subject

import androidx.lifecycle.ViewModel
import com.example.owlfriend.domain.repository.SessionRepository
import com.example.owlfriend.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository
) : ViewModel(){


}