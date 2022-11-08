<?php
require("koneksi.php");
$perintah = "SELECT * FROM tbl_laundry ORDER BY id DESC";
$eksekusi = mysqli_query($konek, $perintah);
$cek = mysqli_affected_rows($konek);

$server_name = $_SERVER['SERVER_ADDR'];

if($cek > 0){
    $response["kode"] = 1;
    $response["pesan"] = "Data Tersedia";
    $response["data"] = array();

    while($ambil = mysqli_fetch_object($eksekusi)){
        $F["id"] = $ambil->id;
        $F["nama"] = $ambil->nama;
        $F["alamat"] = $ambil->alamat;
        $F["telepon"] = $ambil->telepon;
        $F["picture"] = "http://$server_name".$ambil->picture;

        array_push($response["data"], $F);
    }
}
else {
    $response["kode"] = 0;
    $response["pesan"] = "Data Tidak Tersedia";
}

echo json_encode($response);
mysqli_close($konek);

?>