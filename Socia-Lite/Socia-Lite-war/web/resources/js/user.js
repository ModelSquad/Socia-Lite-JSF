$(document).ready(function () {    
    $('.bename').on('click', function(){
        $('#editName').show();
    });
    $('#ename').on('click', function(){
        $('#editName').hide();
    });
    
    $(".besurname").click(function(){
        $("#editSurname").show();
    });
    $('#esurname').on('click', function(){
        $('#editSurname').hide();
    });
    
    $(".bebirthdate").click(function(){
        $("#editBirthdate").show();
    });
    $("#ebirthdate").click(function(){
        $("#editBirthdate").hide();
    });
    
    $('#DOMBirthplace').on('click','.babirthplace', function(){
        $("#addBirthplace").show();
    });
    $("#abirthplace").on('click', function(){
        $("#addBirthplace").hide();
    });
    $('#DOMBirthplace').on('click','.bebirthplace', function(){
        $("#editBirthplace").show();
    });
    $("#ebirthplace").on('click', function(){
        $("#editBirthplace").hide();
    });
    
    $('#DOMJob').on('click','.bajob', function(){
        $("#addJob").show();
    });
    $("#ajob").on('click', function(){
        $("#addJob").hide();
    });
    $('#DOMJob').on('click','.bejob', function(){
        $("#editJob").show();
    });
    $("#ejob").on('click', function(){
        $("#editJob").hide();
    });
    
    $('#DOMStudyPlace').on('click','.bastudyPlace', function(){
        $("#addStudyPlace").show();
    });
    $("#astudyPlace").on('click', function(){
        $("#addStudyPlace").hide();
    });
    $('#DOMStudyPlace').on('click','.bestudyPlace', function(){
        $("#editStudyPlace").show();
    });
    $("#estudyPlace").on('click', function(){
        $("#editStudyPlace").hide();
    });
    
    
    $('#DOMWebsite').on('click','.bawebsite', function(){
        $("#addWebsite").show();
    });
    $("#awebsite").on('click', function(){
        $("#addWebsite").hide();
    });
    $('#DOMWebsite').on('click','.bewebsite', function(){
        $("#editWebsite").show();
    });
    $("#ewebsite").on('click', function(){
        $("#editWebsite").hide();
    });
    
 
});



