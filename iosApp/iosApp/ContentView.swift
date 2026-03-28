import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let controller = MainViewControllerKt.MainViewController()
        // 设置状态栏样式为深色内容（适配白色背景）
        controller.overrideUserInterfaceStyle = .light
        return controller
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    @State var scaleAmount = 1.0
    @State var isHomeRootScreen = false

    var body: some View {
        ZStack {
            if isHomeRootScreen {
                ComposeView()
                    .preferredColorScheme(.light) // 强制使用浅色模式
            } else {
                // 设置背景颜色为白色
                Color.white.ignoresSafeArea()
                
                VStack {
                    // 使用与掘金品牌相关的系统图标
                    Image(systemName: "book.fill")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .scaleEffect(scaleAmount)
                        .frame(width: 120)
                        .foregroundColor(.blue)
                    
                    // 添加应用名称
                    Text("掘金 APP")
                        .font(.largeTitle)
                        .fontWeight(.bold)
                        .foregroundColor(.blue)
                        .padding(.top, 20)
                }
                .onAppear() {
                    // 缩放动画：2秒内将图标缩小到消失
                    withAnimation(.easeIn(duration: 2)) {
                        scaleAmount = 0.0
                    }

                    // 2秒后切换到主应用
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                        isHomeRootScreen = true
                    }
                }
            }
        }
        .ignoresSafeArea((isHomeRootScreen ? .keyboard : .all))
    }
}




