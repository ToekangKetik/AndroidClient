package etrash.datacake.project.kanaksasak.e_trash.Fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import etrash.datacake.project.kanaksasak.e_trash.Config;
import etrash.datacake.project.kanaksasak.e_trash.Data.UploadInfo;
import etrash.datacake.project.kanaksasak.e_trash.R;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class PostFragment extends Fragment {

    private static final int CHOOSING_IMAGE_REQUEST = 1234;
    private StorageReference imageReference;
    private StorageReference fileRef;
    private Uri fileUri;
    private ImageView img_produk;
    private Button btn_post;
    private EditText desc_txt, price_txt;
    private FirebaseDatabase database;
    private DatabaseReference mDataReference;
    private DatabaseReference mDataReference2;
    private ProgressDialog progressDialog;
    private String mUID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_post, container, false);

        img_produk = rootView.findViewById(R.id.produk_img);
        btn_post = rootView.findViewById(R.id.btn_post);
        desc_txt = rootView.findViewById(R.id.desc_txt);
        price_txt = rootView.findViewById(R.id.price_txt);


        GetUID();
        database = FirebaseDatabase.getInstance();
        mDataReference = database.getReference("post/" + mUID + "");
        mDataReference2 = database.getReference("market/");
        fileRef = null;
        progressDialog = new ProgressDialog(this.getContext());

        img_produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoosingFile();
            }
        });

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postOnCloud();
            }
        });

        return rootView;
    }

    private void showChoosingFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), CHOOSING_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
//            final Uri resultUri = UCrop.getOutput(data);
//            Toast.makeText(this.getContext(), "URI : " + resultUri + "", Toast.LENGTH_LONG).show();
//            img_produk.setImageURI(resultUri);
//        } else if (resultCode == UCrop.RESULT_ERROR) {
//            final Throwable cropError = UCrop.getError(data);
        if (requestCode == CHOOSING_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            Toast.makeText(this.getContext(), "URI : " + fileUri + "", Toast.LENGTH_LONG).show();
            CropStart(fileUri);

        }

    }

    private void writeNewImageInfoToDB(String name, String url, String desc, String price) {
        UploadInfo info = new UploadInfo(name, url, desc, price);

        String key = mDataReference.push().getKey();
        mDataReference.child(key).setValue(info);
        mDataReference2.child(key).setValue(info);

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private boolean validateInputFileName(String fileName, String desc, String price) {

        if (TextUtils.isEmpty(fileName) || TextUtils.isEmpty(desc)) {
            Toast.makeText(this.getContext(), "Enter Descriptions!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(price)) {
            Toast.makeText(this.getContext(), "Enter Price!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void postOnCloud() {
        // store image at "images/filename.extension" on Firebase Cloud Storage


        if (fileUri != null) {
            String fileName;
            Long tsLong = System.currentTimeMillis() / 1000;
            fileName = tsLong.toString();

            final String desc = desc_txt.getText().toString();
            final String price = price_txt.getText().toString();

            if (!validateInputFileName(fileName, desc, price)) {
                return;
            }

            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            imageReference = FirebaseStorage.getInstance().getReference().child("Images");
            fileRef = imageReference.child(fileName + "." + getFileExtension(fileUri));
            fileRef.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            String name = taskSnapshot.getMetadata().getName();
                            String url = taskSnapshot.getDownloadUrl().toString();

                            Log.e(TAG, "Uri: " + url);
                            Log.e(TAG, "Name: " + name);

                            writeNewImageInfoToDB(name, url, desc, price);

                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.pager, new PostFragment());
                            transaction.commit();

                            Toast.makeText(getContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();

                            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            // percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    })
                    .addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("Upload is paused!");
                        }
                    });
        } else {
            Toast.makeText(this.getContext(), "No File!", Toast.LENGTH_LONG).show();
        }
    }

    private void CropStart(@NonNull Uri uri) {
        UCrop.Options options = new UCrop.Options();
        UCrop.of(uri, fileUri)
                .withAspectRatio(16, 9)
                .withMaxResultSize(200, 200)
                .start(getActivity());
        img_produk.setImageURI(fileUri);
    }


    private void GetUID() {

        SharedPreferences pref = this.getContext().getSharedPreferences(Config.UID, 0);
        mUID = pref.getString("UID", "0");


    }
}
