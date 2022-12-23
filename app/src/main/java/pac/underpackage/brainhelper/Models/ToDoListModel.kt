package pac.underpackage.brainhelper.Models

import java.time.LocalDateTime

//TODO tady musi byt parametry konstruktoru povinne
class ToDoListModel(ID : Int, title : String, deadline : LocalDateTime, difficulty : Int, importance : Int, completed : Int) :
    Model(ID, title, deadline, difficulty, importance, completed) {


}