package com.example.owlfriend.presentation.session

import com.example.owlfriend.domain.Session
import com.example.owlfriend.domain.Subject

data class SessionState(
    val subjects: List<Subject> = emptyList(),
    val sessions: List<Session> = emptyList(),
    val relatedToSubject: String? = null,
    val subjectId: Int? = null,
    val session: Session? = null
)