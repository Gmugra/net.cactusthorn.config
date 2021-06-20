/*
* Copyright (C) 2021, Alexei Khatskevich
*
* Licensed under the BSD 3-Clause license.
* You may obtain a copy of the License at
*
* https://github.com/Gmugra/net.cactusthorn.config/blob/main/LICENSE
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
* FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
* DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
* CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
* OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package net.cactusthorn.config.core.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.cactusthorn.config.core.Reloadable;

public class AutoReloader {

    private ScheduledExecutorService reloaderService;

    private final Lock startLock = new ReentrantLock();
    private final List<Reloadable> configs = new ArrayList<>();
    private final long periodInSeconds;

    private final Runnable reload = new Runnable() {
        @Override public void run() {
            for (Reloadable config : configs) {
                config.reload();
            }
        }
    };

    public AutoReloader(long periodInSeconds) {
        this.periodInSeconds = periodInSeconds;
    }

    public void register(Reloadable reloadable) {
        //if auto reloading is not configured -> do nothing
        if (periodInSeconds == 0L) {
            return;
        }
        configs.add(reloadable);
        start();
    }

    private void start() {
        //check to avoid lock
        if (reloaderService != null) {
            return;
        }
        startLock.lock();
        try {
            //check again to avoid race condition
            if (reloaderService != null) {
                return;
            }
            ThreadFactory threadFactory = new ThreadFactory() {
                @Override public Thread newThread(Runnable runnable) {
                    Thread thread = new Thread(runnable);
                    thread.setName("config-auto-reloader");
                    return thread;
                }
            };
            reloaderService = Executors.newSingleThreadScheduledExecutor(threadFactory);
            reloaderService.scheduleAtFixedRate(reload, 2, periodInSeconds, TimeUnit.SECONDS);
        } finally {
            startLock.unlock();
        }
    }
}
