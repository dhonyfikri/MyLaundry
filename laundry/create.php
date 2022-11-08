<?php
require("koneksi.php");

$response = array();

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $nama = $_POST["nama"];
    $alamat = $_POST["alamat"];
    $telepon = $_POST["telepon"];
    $picture = $_POST["picture"];

    $perintah = "INSERT INTO tbl_laundry (nama,alamat,telepon,last_update) VALUES('$nama','$alamat','$telepon', now())";
    $eksekusi = mysqli_query($konek, $perintah);
    $cek      = mysqli_affected_rows($konek);

    if($cek > 0){
        if ($picture == null) {

            $id = mysqli_insert_id($konek);
            $finalPath = "/laundry/image/image-laundry/none.jpeg"; 
            $insert_picture = "UPDATE tbl_laundry SET picture='$finalPath' WHERE id='$id' ";
            $jalankanQuery = mysqli_query($konek, $insert_picture);

            $response["kode"] = 1;
            $response["pesan"] = "Simpan Data Berhasil (tanpa foto)";

            echo json_encode($response);
            mysqli_close($konek);

        } else {

            $id = mysqli_insert_id($konek);
            $path = "image/image-laundry/img$id.jpeg";
            $finalPath = "/laundry/".$path;

            $insert_picture = "UPDATE tbl_laundry SET picture='$finalPath' WHERE id='$id' ";
        
            if (mysqli_query($konek, $insert_picture)) {
        
                if ( file_put_contents( $path, base64_decode($picture) ) ) {
                    
                    $response["kode"] = 1;
                    $response["pesan"] = "Simpan Data Berhasil";
        
                    echo json_encode($response);
                    mysqli_close($konek);
        
                } else {
                    
                    $response["kode"] = 0;
                    $response["pesan"] = "Error! ".mysqli_error($konek);
                    echo json_encode($response);

                    mysqli_close($konek);
                }

            }
        }
        
    }
    else{
        $response["kode"] = 0;
        $response["pesan"] = "Gagal Menyimpan Data";
        echo json_encode($response);
        mysqli_close($konek);
    }
}
else{
    $response["kode"] = 0;
    $response["pesan"] = "Tidak Ada Post Data";
    echo json_encode($response);
}

?>