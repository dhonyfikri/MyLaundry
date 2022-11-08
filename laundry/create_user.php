<?php
require("koneksi.php");

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $username = $_POST["username"];
    $password_user = $_POST["password_user"];
    $nama_user = $_POST["nama_user"];
    $alamat_user = $_POST["alamat_user"];
    $foto_user = $_POST["foto_user"];

    $perintah = "INSERT INTO tbl_user (username,password_user,nama_user,alamat_user,last_update) VALUES('$username','$password_user','$nama_user', '$alamat_user', now())";
    $eksekusi = mysqli_query($konek, $perintah);
    $cek      = mysqli_affected_rows($konek);

    if($cek > 0){
        if ($foto_user == null) {

            $id = mysqli_insert_id($konek);
            $finalPath = "/laundry/image/image-user/none.jpeg"; 
            $insert_picture = "UPDATE tbl_user SET foto_user='$finalPath' WHERE id_user='$id' ";
            $jalankanQuery = mysqli_query($konek, $insert_picture);

            $response["kodeUser"] = 1;
            $response["pesanUser"] = "Register Data Berhasil (tanpa foto)";

            echo json_encode($response);
            mysqli_close($konek);

        } else {

            $id = mysqli_insert_id($konek);
            $path = "image/image-user/img$id.jpeg";
            $finalPath = "/laundry/".$path;

            $insert_picture = "UPDATE tbl_user SET foto_user='$finalPath' WHERE id_user='$id' ";
        
            if (mysqli_query($konek, $insert_picture)) {
        
                if ( file_put_contents( $path, base64_decode($foto_user) ) ) {
                    
                    $response["kodeUser"] = 1;
                    $response["pesanUser"] = "Register Data Berhasil";
        
                    echo json_encode($response);
                    mysqli_close($konek);
        
                } else {
                    
                    $response["kodeUser"] = 0;
                    $response["pesanUser"] = "Error! ".mysqli_error($konek);
                    echo json_encode($response);

                    mysqli_close($konek);
                }

            }
        }
        
    }
    else{
        $response["kodeUser"] = 0;
        $response["pesanUser"] = "Gagal Register";
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