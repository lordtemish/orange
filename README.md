# orange
<h3>Пример веб приложения на Spring boot</h3>

<h5>Controllers:</h5>
    <p>  PatientController: Запросы для пациентов</p>
      <p>DoctorController: Запросы для доктора</p>
      <p>MainController: Основные запросы</p>
 
<h5> Регистрация делается через MainController: </h5>
         <p>   1)addCLient(email, password) после чего на email придет код</p>
<p>            2)checkCode(email, code) придет Объект со String token</p>
<p>            3)resend(email, password) на email переотправит код</p>
<h5>Авторизация : надо вставить в header параметр "token" c токеном внутри  </h5>         
   <p>         4) a) DoctorController: addDoctor... </p>
      <p>          b) PatientController: addNew</p>
  
  <h5>Вход MainController :</h5>
  <p>authClient(email, password) при правильных данных выведет токен</p>
        
