<?php
require("koneksi.php");

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $username = $_POST["username"];
    $password_user = $_POST["password_user"];
    $metode = $_POST["metode"];

    if($metode == "cek-login"){
        $perintah = "SELECT COUNT(*) AS usercount FROM tbl_user WHERE BINARY username = '$username' AND BINARY password_user = '$password_user'";
    }
    else if($metode == "cek-duplikat"){
        $perintah = "SELECT COUNT(*) AS usercount FROM tbl_user WHERE username = '$username'";
    }

    $eksekusi = mysqli_query($konek, $perintah);
    $cek = mysqli_affected_rows($konek);

    if($cek > 0){
        $response["kodeLogin"] = 1;
        $response["pesanLogin"] = "Data Tersedia";
        $response["dataLogin"] = array();
    
        while($ambil = mysqli_fetch_object($eksekusi)){
            $F["usercount"] = $ambil->usercount;
    
            array_push($response["dataLogin"], $F);
        }
        echo json_encode($response);
        mysqli_close($konek);
    }
    else {
        $response["kodeLogin"] = 0;
        $response["pesanLogin"] = "Data Tidak Tersedia";
        echo json_encode($response);
        mysqli_close($konek);
    }
}
else{
    $response["kodeLogin"] = 0;
    $response["pesanLogin"] = "Tidak Ada Post Data";
    echo json_encode($response);
}

?>