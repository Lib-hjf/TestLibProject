package com.hjf.test.t_func;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.hjf.base.activity.BaseFragment;
import com.hjf.test.R;
import com.hjf.test.TestStringRecyclerAdapter;

import org.hjf.thread.LinkTask;
import org.hjf.thread.MeshTask;
import org.hjf.view.recyclerview.OnViewClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * MeshRunnable 、 LinkRunnable test
 */
public class TaskDemoFragment extends BaseFragment {

    private TestStringRecyclerAdapter adapter;

    public static BaseFragment newInstance() {
        TaskDemoFragment taskDemoFragment = new TaskDemoFragment();
        return taskDemoFragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.v_recycler_view;
    }

    @Override
    public void bindView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter = new TestStringRecyclerAdapter(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivityInBaseFragment));
        adapter.setOnViewClickListener(new OnViewClickListener() {
            @Override
            public void onViewClickListener(View clickView, int position) {
                String data = adapter.getData(position);
                if (TextUtils.isEmpty(data)) {
                    return;
                }
                switch (data) {
                    case "Mesh Runnable Test":
                        testMeshRunnable();
                        break;
                    case "Link Runnable Test":
                        testLinkRunnable();
                        break;
                    default:
                        break;
                }
            }
        }, R.id.v_textView);
        adapter.setDataList(getData());
    }

    private void testMeshRunnable() {
        MeshTask meshTask = new MeshTask()
                .addRunnable("runnable 1-1", new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ignored) {
                        }
                    }
                })
                .addRunnable("runnable 1-2", new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {
                        }
                    }
                })
                .addRunnable("runnable 1-3", new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ignored) {
                        }
                    }
                })
                .addRunnable("runnable 2-1", new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }, "runnable 1-1")
                .addRunnable("runnable 2-2", new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }, "runnable 1-2")
                .addRunnable("runnable 3-1,2", new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }, "runnable 2-1", "runnable 2-2")
                .addRunnable("runnable 4-1", new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }, "runnable 3-1,2", "runnable 2-2")
                .addRunnable("runnable 4-2", new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }, "runnable 3-1,2");
        meshTask.execute();
    }

    private void testLinkRunnable() {
        LinkTask linkTask = new LinkTask()
                .addRunnableInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgress();
                    }
                })
                .addRunnable(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException ignored) {
                        }
                    }
                })
                .addRunnableInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgress();
                    }
                });
        linkTask.execute();
    }

    private List<String> getData() {
        List<String> dataList = new ArrayList<>();
        dataList.add("Mesh Runnable Test");
        dataList.add("Link Runnable Test");
        return dataList;
    }
}
