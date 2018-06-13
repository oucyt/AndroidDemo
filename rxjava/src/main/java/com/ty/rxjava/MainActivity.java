package com.ty.rxjava;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.text_view)
    TextView mTextView;
    private String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8})
    public void onViewClicked(View view) {
        clearConsole();
        switch (view.getId()) {
            case R.id.btn1:
                create();
                break;
            case R.id.btn2:
                consumer();
                break;
            case R.id.btn3:
                schedulers();
                break;
            case R.id.btn4:
                map();
                break;
            case R.id.btn5:
                zip();
                break;
            case R.id.btn6:
                concat();
                break;
            case R.id.btn7:
                flatMap();
                break;
            case R.id.btn8:
                fun8();
                break;
        }
    }

    private void fun8() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                int delayTime = (int) (1 + Math.random() * 10);
                return Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        appendConsole("flatMap : accept : " + s + "");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void flatMap() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                appendConsole("Integer emit : 1 ");
                e.onNext(2);
                appendConsole("Integer emit : 2 ");
                e.onNext(3);
                appendConsole("Integer emit : 3 ");
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                int delayTime = (int) (1 + Math.random() * 10);
                return Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        appendConsole("flatMap : accept : " + s + "");
                    }
                });

    }

    boolean isFromNet;

    @SuppressLint("CheckResult")
    private void concat() {
        Observable<FoodList> cache = Observable.create(new ObservableOnSubscribe<FoodList>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<FoodList> e) throws Exception {
                FoodList data = CacheManager.getInstance().getFoodListData();

                // 在操作符 concat 中，只有调用 onComplete 之后才会执行下一个 Observable

                if (data != null) { // 如果缓存数据不为空，则直接读取缓存数据，而不读取网络数据
                    isFromNet = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            appendConsole("subscribe: 读取缓存数据:");
                        }
                    });

                    e.onNext(data);
                } else {
                    isFromNet = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            appendConsole("subscribe: 读取网络数据:");
                        }
                    });
                    e.onComplete();
                }


            }
        });

        Observable<FoodList> network = Rx2AndroidNetworking.get("http://www.tngou.net/api/food/list")
                .addQueryParameter("rows", 10 + "")
                .build()
                .getObjectObservable(FoodList.class);


        // 两个 Observable 的泛型应当保持一致

        Observable.concat(cache, network)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FoodList>() {
                    @Override
                    public void accept(@NonNull FoodList tngouBeen) throws Exception {
                        if (isFromNet) {
                            appendConsole("accept : 网络获取数据设置缓存: ");
                            CacheManager.getInstance().setFoodListData(tngouBeen);
                        }

                        appendConsole("accept: 读取数据成功:" + tngouBeen.toString() + "");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        appendConsole("accept: 读取数据失败：" + throwable.getMessage() + "");
                    }
                });

    }

    @SuppressLint("CheckResult")
    private void zip() {
        Observable.zip(getStringObservable(), getIntegerObservable(), new BiFunction<String, Integer, String>() {
            @Override
            public String apply(@NonNull String s, @NonNull Integer integer) throws Exception {
                return s + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                appendConsole("zip : accept : " + s + "");
            }
        });

    }

    private Observable<String> getStringObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                if (!e.isDisposed()) {
                    e.onNext("A");
                    appendConsole("String emit : A ");
                    e.onNext("B");
                    appendConsole("String emit : B ");
                    e.onNext("C");
                    appendConsole("String emit : C ");
                }
            }
        });
    }

    private Observable<Integer> getIntegerObservable() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (!e.isDisposed()) {
                    e.onNext(1);
                    appendConsole("Integer emit : 1 ");
                    e.onNext(2);
                    appendConsole("Integer emit : 2 ");
                    e.onNext(3);
                    appendConsole("Integer emit : 3 ");
                    e.onNext(4);
                    appendConsole("Integer emit : 4 ");
                    e.onNext(5);
                    appendConsole("Integer emit : 5 ");
                }
            }
        });
    }

    @SuppressLint("CheckResult")
    private void map() {
        Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Response> e) throws Exception {
                Request.Builder builder = new Request.Builder()
                        .url("http://api.avatardata.cn/MobilePlace/LookUp?key=ec47b85086be4dc8b5d941f5abd37a4e&mobileNumber=13021671512")
                        .get();
                Request request = builder.build();
                Call call = new OkHttpClient().newCall(request);
                Response response = call.execute();
                e.onNext(response);
            }
        }).map(new Function<Response, MobileAddress>() {
            @Override
            public MobileAddress apply(@NonNull Response response) throws Exception {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        appendConsole("map:转换前:" + response.body());
                        return new Gson().fromJson(body.string(), MobileAddress.class);
                    }
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<MobileAddress>() {
                    @Override
                    public void accept(@NonNull MobileAddress s) {
                        appendConsole("doOnNext: 保存成功：" + s.toString() + "");
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MobileAddress>() {
                    @Override
                    public void accept(@NonNull MobileAddress data) {
                        appendConsole("成功:" + data.toString() + "");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        appendConsole("失败：" + throwable.getMessage() + "");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void schedulers() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                appendConsole("Observable thread is : " + Thread.currentThread().getName());
                e.onNext(1);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        appendConsole("After observeOn(mainThread)，Current thread is " + Thread.currentThread().getName());
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        appendConsole("After observeOn(io)，Current thread is " + Thread.currentThread().getName());
                    }
                });

    }

    //    用于描述一个方法，如果它的返回值被忽略了，那么就会报错
    @SuppressLint("CheckResult")
    private void consumer() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                appendConsole("Observable emit 1");
                emitter.onNext(1);
                appendConsole("Observable emit 2");
                emitter.onNext(2);
                appendConsole("Observable emit 3");
                emitter.onNext(3);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                appendConsole("accpet " + integer);
            }
        });
    }

    private void create() {
        Observable.create(new ObservableOnSubscribe<Integer>() { // 第一步：初始化Observable
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) {
                appendConsole("Observable emit 1");
                e.onNext(1);
                appendConsole("Observable emit 2");
                e.onNext(2);
                appendConsole("Observable emit 3");
                e.onNext(3);
                e.onComplete();
                appendConsole("Observable emit 4");
                e.onNext(4);
            }
        }).subscribe(new Observer<Integer>() { // 第三步：订阅

            // 第二步：初始化Observer
            private int i;
            private Disposable mDisposable;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                appendConsole("onNext " + integer);
                i++;
                if (i == 2) {
                    // 在RxJava 2.x 中，新增的Disposable可以做到切断的操作，让Observer观察者不再接收上游事件
                    mDisposable.dispose();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                appendConsole("onError : value : " + e.getMessage());
            }

            @Override
            public void onComplete() {
                appendConsole("onComplete");
            }
        });
    }

    private void appendConsole(String s) {
        mTextView.setText(mTextView.getText().toString() + "\n" + s);
    }

    private void clearConsole() {
        mTextView.setText("");
    }
}
