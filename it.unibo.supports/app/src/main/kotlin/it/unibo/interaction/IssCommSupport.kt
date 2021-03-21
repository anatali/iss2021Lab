/**
 * IssCommSupport.java
 * ==========================================================================
 *
 * ==========================================================================
 */
package it.unibo.interaction

interface IssCommSupport : IssOperations {
    fun registerObserver(obs: IssObserver)
    fun removeObserver(obs: IssObserver)
    fun close()
}