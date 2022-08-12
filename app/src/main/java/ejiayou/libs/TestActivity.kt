package ejiayou.libs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.orhanobut.logger.Logger
import ejiayou.libs.databinding.TestActivityBinding
import ejiayou.libs.module.api.rxhttp.RxNetModel
import ejiayou.libs.module.api.rxhttp.RxObservableProxy
import ejiayou.libs.module.api.rxhttp.RxObserverInterface
import ejiayou.libs.module.api.rxhttp.RxResultHelper
import ejiayou.libs.module.base.BaseAppBVMActivity
import ejiayou.libs.module.dto.TestBannerBean
import ejiayou.libs.module.http.TestViewModel
import ejiayou.libs.module.router.Router
import ejiayou.libs.module.router.SimpleRouteCallback
import ejiayou.libs.module.ui.BarHelperConfig
import ejiayou.libs.module.ui.BarOnBackListener
import ejiayou.libs.module.ui.BarOnNextListener
import ejiayou.libs.module.ui.ImmersionBarConfig
import ejiayou.libs.module.utils.ToastUtils
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function

/**
 * @author: lr
 * @created on: 2022/7/15 10:30 下午
 * @description:
 */
class TestActivity : BaseAppBVMActivity<TestActivityBinding, TestViewModel>() {
    override fun layoutRes(): Int {
        return R.layout.test_activity
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Logger.d("onActivityResult requestCode = $requestCode - resultCode = $resultCode ")
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        Logger.d("onActivityReenter resultCode = $resultCode ")
    }

    override fun initialize(savedInstanceState: Bundle?) {
        Log.d("TTT", "initialize")
        val manager = supportFragmentManager
        val beginTransaction = manager.beginTransaction()
        val fragment = TestFragment()

        beginTransaction.add(R.id.frameLayout, fragment)

        beginTransaction.commit()
        binding.testArouter.setOnClickListener {
            testARouter()
        }
        binding.testFile.setOnClickListener {
            navigate(TestFileActivity::class.java)
        }


        binding.testHttp.setOnClickListener {
            viewModel.getTestList()
//            testGif()
//            testHttp()
        }
    }

    fun testARouter() {
        Router.path("/aa/bb/test").withString("str", "我是被传递的参数").withInt("age", 16)
            .setCallback(object : SimpleRouteCallback() {
                override fun before() {
                    Logger.d("Router SimpleRouteCallback before")
                }

                override fun after() {
                    Logger.d("Router SimpleRouteCallback after")
                }

                override fun interrupt() {
                    Logger.d("Router SimpleRouteCallback interrupt")
                }

            }).navigation(
                this, 122/*, object : NavigationCallback {
                override fun onFound(postcard: Postcard?) {
                    Logger.d("Router onFound")
                }

                override fun onLost(postcard: Postcard?) {
                    Logger.d("Router onLost")
                }

                override fun onArrival(postcard: Postcard?) {
                    Logger.d("Router onArrival")
                }

                override fun onInterrupt(postcard: Postcard?) {
                    Logger.d("Router onInterrupt")
                }

            }*/
            )
    }

    fun testGif() {
        binding.emptyLoading.loadingEmptyView.start()
        binding.ltGift.setAnimation("lottie/loading.json")
        binding.ltGift.playAnimation()
    }

    fun testHttp() {
        RxObservableProxy.createProxy(
            RxNetModel.search("11")
                .compose(RxResultHelper.handleResult())
                .map(Function { data ->
                    data
                })
        )!!.subscribe(object : RxObserverInterface<TestBannerBean>() {
            override fun throwError(msg: String?) {
                ToastUtils.showToast(applicationContext, "throwError $msg")
            }

            override fun onSubscribe(d: Disposable?) {
                d?.let {
                    ToastUtils.showToast(applicationContext, "onSubscribe ${it.isDisposed}")
                }

            }

            override fun onNext(value: TestBannerBean?) {
                value?.let {
                    ToastUtils.showToast(applicationContext, "onNext ${value.url}")
                }
            }

            override fun onComplete() {
                ToastUtils.showToast(applicationContext, "onComplete ")
            }

        })

    }

    override fun initImmersionBarConfig(): ImmersionBarConfig? {
        return ImmersionBarConfig.builder().build()
    }

    override fun initBarHelperConfig(): BarHelperConfig? {
        return BarHelperConfig.builder()
            .setTitle("哈哈哈")
            .setOnBackListener(object : BarOnBackListener {
                override fun onBackClick() {

                    ToastUtils.showToast(applicationContext, "onBackClick")

                }

            }).setOnNextListener(object : BarOnNextListener {
                override fun onNextClick() {
                    ToastUtils.showToast(applicationContext, "OnNextListener")
                }

            })

            .setBack(true).build()
    }


    override fun layoutViewGroup(): ViewGroup? {
        return null
    }

    override fun layoutView(): View? {
        return null
    }


    override fun createViewModel(): TestViewModel {
        return TestViewModel()
    }
}