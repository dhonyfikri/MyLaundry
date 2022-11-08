<?php
require("koneksi.php");

if($_SERVER['REQUEST_METHOD'] == 'POST'){

    $username = $_POST["username"];

    $perintah = "SELECT * FROM tbl_user WHERE username = '$username'";
    $eksekusi = mysqli_query($konek, $perintah);
    $cek = mysqli_affected_rows($konek);

    $server_name = $_SERVER['SERVER_ADDR'];

    if($cek > 0){
        $response["kodeUser"] = 1;
        $response["pesanUser"] = "Data Tersedia";
        $response["dataUser"] = array();

        while($ambil = mysqli_fetch_object($eksekusi)){
            $F["id_user"] = $ambil->id_user;
            $F["username"] = $ambil->username;
            $F["password_user"] = $ambil->password_user;
            $F["nama_user"] = $ambil->nama_user;
            $F["alamat_user"] = $ambil->alamat_user;
            $F["foto_user"] = "http://$server_name".$ambil->foto_user;

            array_push($response["dataUser"], $F);
        }
        echo json_encode($response);
        mysqli_close($konek);
    }
    else {
        $response["kodeUser"] = 0;
        $response["pesanUser"] = "Data Tidak Tersedia";
        echo json_encode($response);
        mysqli_close($konek);
    }
}
else{
    $response["kodeUser"] = 0;
    $response["pesanUser"] = "Tidak Ada Post Data";
    echo json_encode($response);
}

?>