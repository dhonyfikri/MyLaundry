<?php
require("koneksi.php");

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $id_user = $_POST["id_user"];
    $username = $_POST["username"];
    $password_user = $_POST["password_user"];
    $nama_user = $_POST["nama_user"];
    $alamat_user = $_POST["alamat_user"];
    $foto_user = $_POST["foto_user"];

    $perintah = "UPDATE tbl_user SET nama_user='$nama_user', alamat_user='$alamat_user', last_update = now() WHERE id_user='$id_user'";

    if(mysqli_query($konek, $perintah)){
        if( $foto_user == null){
            $response["kodeUser"] = 1;
            $response["pesanUser"] = "Update Akun Berhasil (tanpa foto)";

            echo json_encode($response);
            mysqli_close($konek);
        }
        else if ($foto_user == "hapusfoto") {

            $finalPath = "/laundry/image/image-user/none.jpeg"; 
            $insert_picture = "UPDATE tbl_user SET foto_user='$finalPath' WHERE id_user='$id_user' ";
            $jalankanQuery = mysqli_query($konek, $insert_picture);

            $response["kodeUser"] = 1;
            $response["pesanUser"] = "Update Akun Berhasil (hapus foto)";

            echo json_encode($response);
            mysqli_close($konek);

        }
        else {

            //$id = mysqli_insert_id($konek);
            $path = "image/image-user/img$id_user.jpeg";
            $finalPath = "/laundry/".$path;

            $insert_picture = "UPDATE tbl_user SET foto_user='$finalPath' WHERE id_user='$id_user' ";
        
            if (mysqli_query($konek, $insert_picture)) {
        
                if ( file_put_contents( $path, base64_decode($foto_user) ) ) {
                    
                    $response["kodeUser"] = 1;
                    $response["pesanUser"] = "Update Akun Berhasil";
        
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
        $response["pesanUser"] = "Gagal Update Akun";
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