package com.android.imagegallery.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.imagegallery.R
import com.android.imagegallery.databinding.ActivityMainBinding
import com.android.imagegallery.model.Image
import com.android.imagegallery.service.DownloadService
import com.android.imagegallery.service.DownloadService.Companion.PARAM_IMAGE_URL
import com.android.imagegallery.ui.fragment.ImageListFragment
import com.android.imagegallery.util.GalleryConstants.TAG_IMAGE_LIST_FRAGMENT
import com.android.imagegallery.util.permissionHandler.ActivityPermissionHandler
import com.android.imagegallery.util.permissionHandler.BasePermissionHandler
import com.android.imagegallery.util.replaceFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector,
    BasePermissionHandler.RequestPermissionListener,
    ImageListFragment.ImageListFragmentCallback {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    private val permissionHandler: ActivityPermissionHandler by lazy {
        ActivityPermissionHandler(
            this,
            permissions = criticalPermissions,
            listener = this
        )
    }

    private var criticalPermissions: MutableSet<String> = mutableSetOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private lateinit var downloadUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        replaceFragment(
            ImageListFragment.newInstance(),
            android.R.id.content,
            TAG_IMAGE_LIST_FRAGMENT,
            false
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHandler.onRequestPermissionsResult(
            requestCode, permissions,
            grantResults
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        permissionHandler.onActivityResult(requestCode)
    }

    override fun onPermissionsGranted(grantedPermissions: Set<String>) {
        val intent = Intent(applicationContext, DownloadService::class.java)
        intent.putExtra(PARAM_IMAGE_URL, downloadUrl)
        startService(intent)
    }

    override fun onPermissionsNotGranted(deniedPermissions: Set<String>) {
//        permissionHandler.requestPermission()
    }

    override fun onShowPermissionRationale(permissions: Set<String>): Boolean {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.permissions_rationale))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                permissionHandler.retryRequestDeniedPermission()
            }
            .show()
        return true // if don't want to show any rationale, just return false here
    }

    override fun onShowSettingRationale(permissions: Set<String>): Boolean {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.permissions_settings_rationale))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.settings)) { _, _ ->
                permissionHandler.requestPermissionInSetting()
            }
            .show()
        return true // if don't want to show any rationale, just return false here
    }

    override fun onDownload(image: Image) {
        downloadUrl = image.largeImageURL
        permissionHandler.requestPermission()
    }
}
