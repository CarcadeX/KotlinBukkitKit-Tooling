package tech.carcadex.kotlinbukkitkit.tooling.menu

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.readText
import org.jetbrains.kotlin.builtins.getReceiverTypeFromFunctionType
import org.jetbrains.kotlin.builtins.getReturnTypeFromFunctionType
import org.jetbrains.kotlin.builtins.isExtensionFunctionType
import org.jetbrains.kotlin.idea.base.psi.getLineNumber
import org.jetbrains.kotlin.idea.caches.resolve.analyze
import org.jetbrains.kotlin.idea.core.resolveType
import org.jetbrains.kotlin.idea.inspections.collections.isCalling
import org.jetbrains.kotlin.idea.refactoring.fqName.fqName
import org.jetbrains.kotlin.idea.refactoring.fqName.getKotlinFqName
import org.jetbrains.kotlin.idea.references.mainReference
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtLambdaExpression
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.psiUtil.collectDescendantsOfType
import org.jetbrains.kotlin.psi.psiUtil.findDescendantOfType
import org.jetbrains.kotlin.resolve.lazy.BodyResolveMode
import org.jetbrains.kotlin.types.typeUtil.isUnit

private val menuFqSupportedNames = listOf(
    "tech.carcadex.kotlinbukkitkit.dsl.menu"
)

fun findMenuDeclaration(
    element: KtCallExpression,
    currentSelectedLine: Int? = null,
    file: VirtualFile
): MenuDeclaration? {
    val simpleName = "menu"
    val nm = file.readText()
    if (!element.isCallFrom(simpleName, menuFqSupportedNames)) return null
    if (!nm.contains("tech.carcadex.kotlinbukkitkit.menu.dsl.menu"))
        return null;
    val menuFunction = element.findDescendantOfType<KtCallExpression> {
        isMenuCall(it)
    } ?: return null

    // TODO: Support named parameters

    val displayName = menuFunction.valueArguments.getOrNull(0)?.text?.replace("\"", "") ?: return null
    val lines = menuFunction.valueArguments.getOrNull(1)?.text?.toIntOrNull() ?: return null

    val menuLambdaBlock = element.findDescendantOfType<KtLambdaExpression> {
        it.isBuilderBlockFor("tech.carcadex.kotlinbukkitkit.menu.dsl.MenuDSL")
    } ?: return null

    val slotFqName = listOf("tech.carcadex.kotlinbukkitkit.menu.dsl.menu.slot")
    val slotSimpleName = "slot"

    val slotCalls = menuLambdaBlock.collectDescendantsOfType<KtCallExpression> {
        it.isCallFrom(slotSimpleName, slotFqName)
    }.mapNotNull {

        val args = it.valueArguments

        // TODO: support named parameters

        val line = args.getOrNull(0)?.text?.toIntOrNull() ?: return@mapNotNull null
        val slot = args.getOrNull(1)?.text?.toIntOrNull() ?: return@mapNotNull null

        // TODO: support just slot: slot(15, item(...)) {}

        val itemValue = args.getOrNull(2) ?: return@mapNotNull null
        val expression = itemValue.getArgumentExpression() ?: return@mapNotNull null

        val materialExpression = expression.findDescendantOfType<KtDotQualifiedExpression> {
            it.text.startsWith("Material")
        } ?: return@mapNotNull null

        val typeExpression = materialExpression.selectorExpression as? KtNameReferenceExpression ?: return@mapNotNull null
        val materialName = typeExpression.text ?: return@mapNotNull null

        val isSelected = it.getLineNumber() == currentSelectedLine

        MenuSlotDeclaration(
            line,
            slot,
            materialName,
            isSelected
        )
    }

    return MenuDeclaration(displayName, lines, slotCalls)
}

fun isMenuCall(it: KtCallExpression) =
    it.isCallFrom("menu", menuFqSupportedNames)

fun KtLambdaExpression.isBuilderBlockFor(fqName: String): Boolean {
    return (text.replace(" ", "").replace("\t", "").replace("\n", "")
        .contains(Regex("\\{[\\s\\S]*?slot")))  && (parent.parent.text.contains("menu"))
//    val type = resolveType()
//
//    val extensionFunctionType = type?.isExtensionFunctionType
//
//    if (extensionFunctionType != true) return false
//
//    val returnTypeFromFunctionType = type.getReturnTypeFromFunctionType()
//    val receiverTypeFromFunctionType = type.getReceiverTypeFromFunctionType() ?: return false
//    val receiverFqName = receiverTypeFromFunctionType.fqName ?: return false
//
//    return returnTypeFromFunctionType.isUnit() && receiverFqName.asString().equals(fqName)
}

fun KtCallExpression.isCallFrom(
    simpleName: String,
    fqNames: List<String>

): Boolean {
    return text.startsWith(simpleName)
    }