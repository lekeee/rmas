package com.example.nightvibe.repositories

import com.example.nightvibe.models.Mark
import com.example.nightvibe.models.Place
import com.example.nightvibe.services.DatabaseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MarkRepositoryImplementation : MarkRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val databaseService =  DatabaseService(firestore)

    override suspend fun getMarks(placeId: String): Resource<List<Mark>> {
        return try {
            val markReference = firestore.collection("marks")
            val querySnapshot = markReference.get().await()
            val marksList = mutableListOf<Mark>()
            for (document in querySnapshot.documents) {
                val pid = document.getString("placeId") ?: ""
                if (pid == placeId) {
                    marksList.add(
                        Mark(
                            id = document.id,
                            userId = document.getString("userId") ?: "",
                            placeId = placeId,
                            mark = document.getLong("mark")?.toInt() ?: 0
                        )
                    )
                }
            }
            Resource.Success(marksList)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUserMarks(): Resource<List<Mark>> {
        return try{
            val markReference = firestore.collection("marks")
            val querySnapshot = markReference.get().await()
            val marksList = mutableListOf<Mark>()
            for(document in querySnapshot.documents){
                val userId = document.getString("userId") ?: ""
                if(userId == firebaseAuth.currentUser?.uid){
                    marksList.add(Mark(
                        id = document.id,
                        userId = userId,
                        placeId = document.getString("placeId") ?: "",
                        mark = document.getLong("mark")?.toInt() ?: 0
                    ))
                }
            }
            Resource.Success(marksList)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun addMark(placeId: String, mark: Int, place: Place): Resource<String> {
        return try {
            val obj = Mark(
                userId =  firebaseAuth.currentUser!!.uid,
                placeId = placeId,
                mark = mark
            )
            databaseService.addScore(place.userId, mark)
            val id = databaseService.saveMark(obj)
            id
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun updateMark(markId: String, mark: Int): Resource<String> {
        return try {
            val id = databaseService.updateMark(markId, mark)
            id
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}